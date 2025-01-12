package BlockChain.BlockChain

import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.methods.response.EthGetBalance
import org.web3j.protocol.http.HttpService
import org.web3j.tx.gas.ContractGasProvider
import org.web3j.tx.gas.StaticGasProvider
import java.math.BigInteger

object BlockchainUtils {
    // Update the RPC URL to Ganache's default address
    private const val GANACHE_URL = "http://10.53.23.12:7545"
    // Use a private key from the Ganache UI
    private const val PRIVATE_KEY = "0x29aaf17eb5ed02c27935a5492f4b73eacbdc9d78d911ffb7c84cb6ea5e765496"
    // Set a lower gas price and higher gas limit suitable for Ganache
    private const val GAS_PRICE = 20_000_000_000L // 20 Gwei
    private const val GAS_LIMIT = 4_000_000L // Higher gas limit for Ganache

    private val web3j: Web3j by lazy {
        Web3j.build(HttpService(GANACHE_URL))
    }

    private val credentials: Credentials by lazy {
        Credentials.create(PRIVATE_KEY)
    }

    private val gasProvider: ContractGasProvider by lazy {
        StaticGasProvider(
            BigInteger.valueOf(GAS_PRICE),
            BigInteger.valueOf(GAS_LIMIT)
        )
    }

    // Check account balance
    fun getAccountBalance(): BigInteger {
        return try {
            val ethGetBalance: EthGetBalance = web3j.ethGetBalance(credentials.address, org.web3j.protocol.core.DefaultBlockParameterName.LATEST).send()
            ethGetBalance.balance
        } catch (e: Exception) {
            println("Error fetching balance: ${e.message}")
            BigInteger.ZERO
        }
    }

    // Ensure account has sufficient funds
    fun ensureSufficientFunds() {
        val balance = getAccountBalance()
        val requiredGas = BigInteger.valueOf(GAS_PRICE).multiply(BigInteger.valueOf(GAS_LIMIT))
        if (balance < requiredGas) {
            throw IllegalStateException("Insufficient funds for gas: Balance = $balance, Required = $requiredGas")
        }
    }

    fun loadContract(contractAddress: String): DonationContract {
        // Ensure sufficient funds before loading the contract
        ensureSufficientFunds()

        return DonationContract.load(
            contractAddress,
            web3j,
            credentials,
            gasProvider
        )
    }
}
