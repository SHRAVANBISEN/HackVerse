package BlockChain.BlockChain;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Array;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple6;
import org.web3j.tuples.generated.Tuple7;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/hyperledger/web3j/tree/main/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 1.6.1.
 */
@SuppressWarnings("rawtypes")
public class DonationContract extends Contract {
    public static final String BINARY = "608060405234801561001057600080fd5b50611368806100206000396000f3fe608060405234801561001057600080fd5b506004361061004c5760003560e01c80635adce332146100515780638205843114610087578063cf93dda7146100a3578063f8626af8146100d3575b600080fd5b61006b60048036038101906100669190610cab565b610108565b60405161007e9796959493929190610f95565b60405180910390f35b6100a1600480360381019061009c9190610cec565b610586565b005b6100bd60048036038101906100b89190610cab565b61071e565b6040516100ca91906110b2565b60405180910390f35b6100ed60048036038101906100e89190610e1a565b61074c565b6040516100ff9695949392919061102e565b60405180910390f35b6060806060806060806000806001896040516101249190610f7e565b90815260200160405180910390205490506000808281548110610170577f4e487b7100000000000000000000000000000000000000000000000000000000600052603260045260246000fd5b90600052602060002090600702016040518060e00160405290816000820180546101999061124b565b80601f01602080910402602001604051908101604052809291908181526020018280546101c59061124b565b80156102125780601f106101e757610100808354040283529160200191610212565b820191906000526020600020905b8154815290600101906020018083116101f557829003601f168201915b5050505050815260200160018201805461022b9061124b565b80601f01602080910402602001604051908101604052809291908181526020018280546102579061124b565b80156102a45780601f10610279576101008083540402835291602001916102a4565b820191906000526020600020905b81548152906001019060200180831161028757829003601f168201915b5050505050815260200160028201805480602002602001604051908101604052809291908181526020016000905b8282101561037e5783829060005260206000200180546102f19061124b565b80601f016020809104026020016040519081016040528092919081815260200182805461031d9061124b565b801561036a5780601f1061033f5761010080835404028352916020019161036a565b820191906000526020600020905b81548152906001019060200180831161034d57829003601f168201915b5050505050815260200190600101906102d2565b5050505081526020016003820180546103969061124b565b80601f01602080910402602001604051908101604052809291908181526020018280546103c29061124b565b801561040f5780601f106103e45761010080835404028352916020019161040f565b820191906000526020600020905b8154815290600101906020018083116103f257829003601f168201915b505050505081526020016004820180546104289061124b565b80601f01602080910402602001604051908101604052809291908181526020018280546104549061124b565b80156104a15780601f10610476576101008083540402835291602001916104a1565b820191906000526020600020905b81548152906001019060200180831161048457829003601f168201915b505050505081526020016005820180546104ba9061124b565b80601f01602080910402602001604051908101604052809291908181526020018280546104e69061124b565b80156105335780601f1061050857610100808354040283529160200191610533565b820191906000526020600020905b81548152906001019060200180831161051657829003601f168201915b505050505081526020016006820154815250509050806000015181602001518260400151836060015184608001518560a001518660c0015198509850985098509850985098505050919395979092949650565b60006040518060e001604052808981526020018881526020018781526020018681526020018581526020018481526020018381525090806001815401808255809150506001900390600052602060002090600702016000909190919091506000820151816000019080519060200190610600929190610a40565b50602082015181600101908051906020019061061d929190610a40565b50604082015181600201908051906020019061063a929190610ac6565b506060820151816003019080519060200190610657929190610a40565b506080820151816004019080519060200190610674929190610a40565b5060a0820151816005019080519060200190610691929190610a40565b5060c08201518160060155505060016000805490506106b091906111cb565b6001886040516106c09190610f7e565b9081526020016040518091039020819055507fb37325e50125a38bc1090bc5b023807dfa24a1b153841adc0dadf1150099431d8787878787878760405161070d9796959493929190610f95565b60405180910390a150505050505050565b6001818051602081018201805184825260208301602085012081835280955050505050506000915090505481565b6000818154811061075c57600080fd5b906000526020600020906007020160009150905080600001805461077f9061124b565b80601f01602080910402602001604051908101604052809291908181526020018280546107ab9061124b565b80156107f85780601f106107cd576101008083540402835291602001916107f8565b820191906000526020600020905b8154815290600101906020018083116107db57829003601f168201915b50505050509080600101805461080d9061124b565b80601f01602080910402602001604051908101604052809291908181526020018280546108399061124b565b80156108865780601f1061085b57610100808354040283529160200191610886565b820191906000526020600020905b81548152906001019060200180831161086957829003601f168201915b50505050509080600301805461089b9061124b565b80601f01602080910402602001604051908101604052809291908181526020018280546108c79061124b565b80156109145780601f106108e957610100808354040283529160200191610914565b820191906000526020600020905b8154815290600101906020018083116108f757829003601f168201915b5050505050908060040180546109299061124b565b80601f01602080910402602001604051908101604052809291908181526020018280546109559061124b565b80156109a25780601f10610977576101008083540402835291602001916109a2565b820191906000526020600020905b81548152906001019060200180831161098557829003601f168201915b5050505050908060050180546109b79061124b565b80601f01602080910402602001604051908101604052809291908181526020018280546109e39061124b565b8015610a305780601f10610a0557610100808354040283529160200191610a30565b820191906000526020600020905b815481529060010190602001808311610a1357829003601f168201915b5050505050908060060154905086565b828054610a4c9061124b565b90600052602060002090601f016020900481019282610a6e5760008555610ab5565b82601f10610a8757805160ff1916838001178555610ab5565b82800160010185558215610ab5579182015b82811115610ab4578251825591602001919060010190610a99565b5b509050610ac29190610b26565b5090565b828054828255906000526020600020908101928215610b15579160200282015b82811115610b14578251829080519060200190610b04929190610a40565b5091602001919060010190610ae6565b5b509050610b229190610b43565b5090565b5b80821115610b3f576000816000905550600101610b27565b5090565b5b80821115610b635760008181610b5a9190610b67565b50600101610b44565b5090565b508054610b739061124b565b6000825580601f10610b855750610ba4565b601f016020900490600052602060002090810190610ba39190610b26565b5b50565b6000610bba610bb5846110fe565b6110cd565b9050808382526020820190508260005b85811015610bfa5781358501610be08882610c6c565b845260208401935060208301925050600181019050610bca565b5050509392505050565b6000610c17610c128461112a565b6110cd565b905082815260208101848484011115610c2f57600080fd5b610c3a848285611209565b509392505050565b600082601f830112610c5357600080fd5b8135610c63848260208601610ba7565b91505092915050565b600082601f830112610c7d57600080fd5b8135610c8d848260208601610c04565b91505092915050565b600081359050610ca58161131b565b92915050565b600060208284031215610cbd57600080fd5b600082013567ffffffffffffffff811115610cd757600080fd5b610ce384828501610c6c565b91505092915050565b600080600080600080600060e0888a031215610d0757600080fd5b600088013567ffffffffffffffff811115610d2157600080fd5b610d2d8a828b01610c6c565b975050602088013567ffffffffffffffff811115610d4a57600080fd5b610d568a828b01610c6c565b965050604088013567ffffffffffffffff811115610d7357600080fd5b610d7f8a828b01610c42565b955050606088013567ffffffffffffffff811115610d9c57600080fd5b610da88a828b01610c6c565b945050608088013567ffffffffffffffff811115610dc557600080fd5b610dd18a828b01610c6c565b93505060a088013567ffffffffffffffff811115610dee57600080fd5b610dfa8a828b01610c6c565b92505060c0610e0b8a828b01610c96565b91505092959891949750929550565b600060208284031215610e2c57600080fd5b6000610e3a84828501610c96565b91505092915050565b6000610e4f8383610ecc565b905092915050565b6000610e628261116a565b610e6c818561118d565b935083602082028501610e7e8561115a565b8060005b85811015610eba5784840389528151610e9b8582610e43565b9450610ea683611180565b925060208a01995050600181019050610e82565b50829750879550505050505092915050565b6000610ed782611175565b610ee1818561119e565b9350610ef1818560208601611218565b610efa8161130a565b840191505092915050565b6000610f1082611175565b610f1a81856111af565b9350610f2a818560208601611218565b610f338161130a565b840191505092915050565b6000610f4982611175565b610f5381856111c0565b9350610f63818560208601611218565b80840191505092915050565b610f78816111ff565b82525050565b6000610f8a8284610f3e565b915081905092915050565b600060e0820190508181036000830152610faf818a610f05565b90508181036020830152610fc38189610f05565b90508181036040830152610fd78188610e57565b90508181036060830152610feb8187610f05565b90508181036080830152610fff8186610f05565b905081810360a08301526110138185610f05565b905061102260c0830184610f6f565b98975050505050505050565b600060c08201905081810360008301526110488189610f05565b9050818103602083015261105c8188610f05565b905081810360408301526110708187610f05565b905081810360608301526110848186610f05565b905081810360808301526110988185610f05565b90506110a760a0830184610f6f565b979650505050505050565b60006020820190506110c76000830184610f6f565b92915050565b6000604051905081810181811067ffffffffffffffff821117156110f4576110f36112db565b5b8060405250919050565b600067ffffffffffffffff821115611119576111186112db565b5b602082029050602081019050919050565b600067ffffffffffffffff821115611145576111446112db565b5b601f19601f8301169050602081019050919050565b6000819050602082019050919050565b600081519050919050565b600081519050919050565b6000602082019050919050565b600082825260208201905092915050565b600082825260208201905092915050565b600082825260208201905092915050565b600081905092915050565b60006111d6826111ff565b91506111e1836111ff565b9250828210156111f4576111f361127d565b5b828203905092915050565b6000819050919050565b82818337600083830152505050565b60005b8381101561123657808201518184015260208101905061121b565b83811115611245576000848401525b50505050565b6000600282049050600182168061126357607f821691505b60208210811415611277576112766112ac565b5b50919050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052601160045260246000fd5b7f4e487b7100000000000000000000000000000000000000000000000000000000600052602260045260246000fd5b7f4e487b7100000000000000000000000000000000000000000000000000000000600052604160045260246000fd5b6000601f19601f8301169050919050565b611324816111ff565b811461132f57600080fd5b5056fea2646970667358221220bc06509d2ff30e4107e8707f90ef1105b1511ba05175d9f3a524518167f700e764736f6c63430008000033";

    private static String librariesLinkedBinary;

    public static final String FUNC_ADDDONATION = "addDonation";

    public static final String FUNC_DONATIONINDEX = "donationIndex";

    public static final String FUNC_DONATIONS = "donations";

    public static final String FUNC_GETDONATION = "getDonation";

    public static final Event DONATIONADDED_EVENT = new Event("DonationAdded", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<DynamicArray<Utf8String>>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}));
    ;

    @Deprecated
    protected DonationContract(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected DonationContract(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected DonationContract(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected DonationContract(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<TransactionReceipt> addDonation(String id, String userEmail,
            List<String> items, String addressDetails, String pickupSchedule, String userId,
            BigInteger createdAt) {
        final Function function = new Function(
                FUNC_ADDDONATION, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(id), 
                new org.web3j.abi.datatypes.Utf8String(userEmail), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Utf8String>(
                        org.web3j.abi.datatypes.Utf8String.class,
                        org.web3j.abi.Utils.typeMap(items, org.web3j.abi.datatypes.Utf8String.class)), 
                new org.web3j.abi.datatypes.Utf8String(addressDetails), 
                new org.web3j.abi.datatypes.Utf8String(pickupSchedule), 
                new org.web3j.abi.datatypes.Utf8String(userId), 
                new org.web3j.abi.datatypes.generated.Uint256(createdAt)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public static List<DonationAddedEventResponse> getDonationAddedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(DONATIONADDED_EVENT, transactionReceipt);
        ArrayList<DonationAddedEventResponse> responses = new ArrayList<DonationAddedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            DonationAddedEventResponse typedResponse = new DonationAddedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.id = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.userEmail = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.items = (List<String>) ((Array) eventValues.getNonIndexedValues().get(2)).getNativeValueCopy();
            typedResponse.addressDetails = (String) eventValues.getNonIndexedValues().get(3).getValue();
            typedResponse.pickupSchedule = (String) eventValues.getNonIndexedValues().get(4).getValue();
            typedResponse.userId = (String) eventValues.getNonIndexedValues().get(5).getValue();
            typedResponse.createdAt = (BigInteger) eventValues.getNonIndexedValues().get(6).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static DonationAddedEventResponse getDonationAddedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(DONATIONADDED_EVENT, log);
        DonationAddedEventResponse typedResponse = new DonationAddedEventResponse();
        typedResponse.log = log;
        typedResponse.id = (String) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.userEmail = (String) eventValues.getNonIndexedValues().get(1).getValue();
        typedResponse.items = (List<String>) ((Array) eventValues.getNonIndexedValues().get(2)).getNativeValueCopy();
        typedResponse.addressDetails = (String) eventValues.getNonIndexedValues().get(3).getValue();
        typedResponse.pickupSchedule = (String) eventValues.getNonIndexedValues().get(4).getValue();
        typedResponse.userId = (String) eventValues.getNonIndexedValues().get(5).getValue();
        typedResponse.createdAt = (BigInteger) eventValues.getNonIndexedValues().get(6).getValue();
        return typedResponse;
    }

    public Flowable<DonationAddedEventResponse> donationAddedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getDonationAddedEventFromLog(log));
    }

    public Flowable<DonationAddedEventResponse> donationAddedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(DONATIONADDED_EVENT));
        return donationAddedEventFlowable(filter);
    }

    public RemoteFunctionCall<BigInteger> donationIndex(String param0) {
        final Function function = new Function(FUNC_DONATIONINDEX, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<Tuple6<String, String, String, String, String, BigInteger>> donations(
            BigInteger param0) {
        final Function function = new Function(FUNC_DONATIONS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}));
        return new RemoteFunctionCall<Tuple6<String, String, String, String, String, BigInteger>>(function,
                new Callable<Tuple6<String, String, String, String, String, BigInteger>>() {
                    @Override
                    public Tuple6<String, String, String, String, String, BigInteger> call() throws
                            Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple6<String, String, String, String, String, BigInteger>(
                                (String) results.get(0).getValue(), 
                                (String) results.get(1).getValue(), 
                                (String) results.get(2).getValue(), 
                                (String) results.get(3).getValue(), 
                                (String) results.get(4).getValue(), 
                                (BigInteger) results.get(5).getValue());
                    }
                });
    }

    public RemoteFunctionCall<Tuple7<String, String, List<String>, String, String, String, BigInteger>> getDonation(
            String id) {
        final Function function = new Function(FUNC_GETDONATION, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(id)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<DynamicArray<Utf8String>>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}));
        return new RemoteFunctionCall<Tuple7<String, String, List<String>, String, String, String, BigInteger>>(function,
                new Callable<Tuple7<String, String, List<String>, String, String, String, BigInteger>>() {
                    @Override
                    public Tuple7<String, String, List<String>, String, String, String, BigInteger> call(
                            ) throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple7<String, String, List<String>, String, String, String, BigInteger>(
                                (String) results.get(0).getValue(), 
                                (String) results.get(1).getValue(), 
                                convertToNative((List<Utf8String>) results.get(2).getValue()), 
                                (String) results.get(3).getValue(), 
                                (String) results.get(4).getValue(), 
                                (String) results.get(5).getValue(), 
                                (BigInteger) results.get(6).getValue());
                    }
                });
    }

    @Deprecated
    public static DonationContract load(String contractAddress, Web3j web3j,
            Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new DonationContract(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static DonationContract load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new DonationContract(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static DonationContract load(String contractAddress, Web3j web3j,
            Credentials credentials, ContractGasProvider contractGasProvider) {
        return new DonationContract(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static DonationContract load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new DonationContract(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<DonationContract> deploy(Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return deployRemoteCall(DonationContract.class, web3j, credentials, contractGasProvider, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<DonationContract> deploy(Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(DonationContract.class, web3j, credentials, gasPrice, gasLimit, getDeploymentBinary(), "");
    }

    public static RemoteCall<DonationContract> deploy(Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(DonationContract.class, web3j, transactionManager, contractGasProvider, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<DonationContract> deploy(Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(DonationContract.class, web3j, transactionManager, gasPrice, gasLimit, getDeploymentBinary(), "");
    }


    private static String getDeploymentBinary() {
        if (librariesLinkedBinary != null) {
            return librariesLinkedBinary;
        } else {
            return BINARY;
        }
    }

    public static class DonationAddedEventResponse extends BaseEventResponse {
        public String id;

        public String userEmail;

        public List<String> items;

        public String addressDetails;

        public String pickupSchedule;

        public String userId;

        public BigInteger createdAt;
    }
}
