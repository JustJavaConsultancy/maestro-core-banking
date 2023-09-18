package ng.com.justjava.corebanking.util;

import ng.com.justjava.corebanking.service.IpgSynchTransactionService;
import ng.com.justjava.corebanking.service.JournalService;
import ng.com.justjava.corebanking.service.TransactionLogService;
import ng.com.justjava.corebanking.service.WalletAccountService;
import ng.com.justjava.corebanking.service.kafka.producer.TransProducer;
import ng.com.justjava.corebanking.service.mapper.IpgSynchTransactionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class NAVSATransactionScheduler {

    private final Logger log = LoggerFactory.getLogger(NAVSATransactionScheduler.class);

    private final IpgSynchTransactionService ipgSynchTransactionService;
    private final IpgSynchTransactionMapper ipgSynchTransactionMapper;
    private final WalletAccountService walletAccountService;
    private final Utility utility;
    private final TransProducer producer;
    private final TransactionLogService transactionLogService;
    private final JournalService journalService;

    public NAVSATransactionScheduler(IpgSynchTransactionService ipgSynchTransactionService, IpgSynchTransactionMapper ipgSynchTransactionMapper, WalletAccountService walletAccountService, Utility utility, TransProducer producer, TransactionLogService transactionLogService, JournalService journalService) {
        this.ipgSynchTransactionService = ipgSynchTransactionService;
        this.ipgSynchTransactionMapper = ipgSynchTransactionMapper;
        this.walletAccountService = walletAccountService;
        this.utility = utility;
        this.producer = producer;
        this.transactionLogService = transactionLogService;
        this.journalService = journalService;
    }


    @Scheduled(cron = "0 0 0/1 1/1 * ?")
    public void processNavsaTransaction() throws Exception {
        /*List<IpgSynchTransaction> transactions = ipgSynchTransactionService.findByStatus(NavsaTransactionStatus.NEW.getName());
        log.info("Retrieved transactions ===> " + transactions);

        for (IpgSynchTransaction ipgSynchTransaction : transactions) {

            FundDTO byRrr = transactionLogService.findByRrr(ipgSynchTransaction.getTransactionRef());
            if (byRrr != null) {
                try {
                    ipgSynchTransaction.setStatus(NavsaTransactionStatus.PROCESSING.getName());
                    ipgSynchTransactionService.save(ipgSynchTransactionMapper.toDto(ipgSynchTransaction));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                continue;
            }

            log.info("NAVSA transaction being processed " + ipgSynchTransaction);
            FundDTO fundDTO = new FundDTO();
            fundDTO.setNarration(ipgSynchTransaction.getSourceNarration() + " " + ipgSynchTransaction.getDestinationNarration());
            String transRef = utility.getUniqueTransRef();
            log.info("Generated TransRef for NAVSA ===> " + transRef);
            fundDTO.setTransRef(transRef);

            WalletAccount sourceAccount = walletAccountService.findOneByAccountNumber(ipgSynchTransaction.getSourceAccount());
            WalletAccount destinationAccount = walletAccountService.findOneByAccountNumber(ipgSynchTransaction.getDestinationAccount());
            if (sourceAccount != null && destinationAccount != null) {
                fundDTO.setSourceAccountNumber(ipgSynchTransaction.getSourceAccount());
                fundDTO.setSourceBankCode(ipgSynchTransaction.getSourceAccountBankCode());
                fundDTO.setChannel("walletToWallet");
                fundDTO.setAmount(ipgSynchTransaction.getAmount().doubleValue());
                fundDTO.setAccountNumber(ipgSynchTransaction.getDestinationAccount());

                if (ipgSynchTransaction.getTransactionType().equalsIgnoreCase("WALLET_TOPUP")) {
                    fundDTO.setSpecificChannel(SpecificChannel.FUND_WALLET.getName());
                } else if (ipgSynchTransaction.getTransactionType().equalsIgnoreCase("FUNDS_TRANSFER")) {
                    fundDTO.setSpecificChannel(SpecificChannel.SEND_MONEY_INTRA.getName());
                } else if (ipgSynchTransaction.getTransactionType().equalsIgnoreCase("CASH_OUT")) {
                    fundDTO.setSpecificChannel(SpecificChannel.SEND_MONEY.getName());
                }

                fundDTO.setBeneficiaryName(ipgSynchTransaction.getDestinationAccountName());
                fundDTO.setSourceAccountName(ipgSynchTransaction.getSourceAccountName());
                fundDTO.setDestBankCode(ipgSynchTransaction.getDestinationAccountBankCode());
                fundDTO.setRrr(ipgSynchTransaction.getTransactionRef());

                log.info("NAVSA fundDTO sent to kafka ===>  " + fundDTO);

                producer.send(fundDTO);

                try {
                    ipgSynchTransaction.setStatus(NavsaTransactionStatus.PROCESSING.getName());
                    ipgSynchTransactionService.save(ipgSynchTransactionMapper.toDto(ipgSynchTransaction));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
*/
    }

    @Scheduled(cron = "0 10 0/1 1/1 * ?")
    public void changeStatus() {
//        List<IpgSynchTransaction> transactions = ipgSynchTransactionService.findByStatus(NavsaTransactionStatus.NEW.getName());
//        for (IpgSynchTransaction ipgSynchTransaction : transactions) {
//
//            log.info("NAVSA change status transaction  ====> " + ipgSynchTransaction);
//            String transactionRef = ipgSynchTransaction.getTransactionRef();
//
//            List<Journal> journals = journalService.findByExternalRef(transactionRef);
//            if (!journals.isEmpty()) {
//                List<IpgSynchTransaction> update = ipgSynchTransactionService.changeStatus(transactionRef, NavsaTransactionStatus.PROCESSED);
//                log.info("PROCESSED NAVSA transaction ====> " + update);
//            }
//        }
    }
}
