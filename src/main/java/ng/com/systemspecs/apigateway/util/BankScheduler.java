package ng.com.systemspecs.apigateway.util;

import ng.com.systemspecs.apigateway.domain.Bank;
import ng.com.systemspecs.apigateway.service.BankService;
import ng.com.systemspecs.apigateway.service.RITSService;
import ng.com.systemspecs.apigateway.service.WalletAccountTypeService;
import ng.com.systemspecs.apigateway.service.dto.BankDTO;
import ng.com.systemspecs.apigateway.service.dto.WalletAccountTypeDTO;
import ng.com.systemspecs.remitarits.bankenquiry.Banks;
import ng.com.systemspecs.remitarits.bankenquiry.GetActiveBankResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class BankScheduler {

    private final Logger log = LoggerFactory.getLogger(BankScheduler.class);

    private final RITSService ritsService;
    private final BankService bankService;
    private final WalletAccountTypeService walletAccountTypeService;


    public BankScheduler(RITSService ritsService, BankService bankService, WalletAccountTypeService walletAccountTypeService) {
        this.ritsService = ritsService;
        this.bankService = bankService;
        this.walletAccountTypeService = walletAccountTypeService;
    }

    //        @Scheduled(cron = "0 0 0 * * *")
//    @Scheduled(fixedRate = 1000 * 60 * 60 * 60 * 7)
//    @Scheduled(cron = "0 30 9 1 * ?")
    public void populateDbWithBanks() throws Exception {

        GetActiveBankResponse activeBanks = ritsService.getActiveBanks();
        if (activeBanks != null && activeBanks.getStatus().equalsIgnoreCase("success")) {
            List<Banks> banks = activeBanks.getData().getBanks();
            banks.forEach(bank -> {
                if (!bankService.findByBankCode(bank.getBankCode()).isPresent()) {
                        BankDTO bankDTO = new BankDTO();
                        bankDTO.setBankAccronym(bank.getBankAccronym());
                        bankDTO.setBankCode(bank.getBankCode());
                        bankDTO.setBankName(bank.getBankName());
                        bankDTO.setType(bank.getType());

                        BankDTO save = bankService.save(bankDTO);
                        log.info("SAVED BANK " + save);
                } else {
                    log.info("SKIPPING BANK ====> " + bank.getBankName());
                }
            });
        }
    }


    @Scheduled(fixedRate = 1000 * 60 * 60 * 60 * 7)
    public void createWalletAccountTypeOfBonus() throws Exception {

        Optional<WalletAccountTypeDTO> one = walletAccountTypeService.findByAccountTypeId(4L);

        if (!one.isPresent()) {
            WalletAccountTypeDTO walletAccountTypeDTO = new WalletAccountTypeDTO();
            walletAccountTypeDTO.setAccountypeID(4L);
            walletAccountTypeDTO.setWalletAccountType("BonusPoint");
            WalletAccountTypeDTO save = walletAccountTypeService.save(walletAccountTypeDTO);
            log.error("Saved WalletAccountType === > " + save);
        }

    }

//    @Scheduled(fixedRate = 1000 * 60 * 60 * 60 * 7)
//    public void deleteUnUsedStanbicBank() throws Exception {
//
//        Optional<Bank> stanbicBankOptional = bankService.findByBankCode("221");
//        stanbicBankOptional.ifPresent(bank -> bankService.delete(bank.getId()));
//    }

    @Scheduled(fixedRate = 200000000L)
    //    @Scheduled(fixedRate = 1000 * 60 * 60 * 60 * 7)
//    @Scheduled(cron = "0 20 0/1 1/1 * ?")
    public void AddShortCodeToBanks() throws Exception {

        HashMap<String, String> bankCodeMap = new HashMap<>();
        bankCodeMap.put("044", "901");
        bankCodeMap.put("050", "326");
        bankCodeMap.put("214", "329");
        bankCodeMap.put("070", "770");
        bankCodeMap.put("011", "894");
        bankCodeMap.put("058", "737");
        bankCodeMap.put("030", "745");
        bankCodeMap.put("082", "7111");
        bankCodeMap.put("221", "909");
        bankCodeMap.put("033", "919");
        bankCodeMap.put("215", "7799");
        bankCodeMap.put("032", "826");
        bankCodeMap.put("035", "945");
        bankCodeMap.put("057", "966");
        bankCodeMap.put("232", "822");
        bankCodeMap.put("063", "426");

        for (Map.Entry<String, String> entry : bankCodeMap.entrySet()) {
            Optional<Bank> byBankCode = bankService.findByBankCode(entry.getKey());
            if (byBankCode.isPresent()) {
                Bank bank = byBankCode.get();
                bank.setShortCode(entry.getValue());
                Bank save = bankService.save(bank);
                log.info("Updated Bank short code ===> " + save);
            }
        }


    }





     /*@Scheduled(fixedRate = 1000 * 1000 * 7)
    public void renameSpecialAccountName() {
        List<WalletAccount> specialWallets = walletAccountRepository.findAllByAccountOwnerIsNull();
        for (WalletAccount walletAccount : specialWallets) {
            Optional<Biller> byBillerID = billerService.findByBillerID(walletAccount.getAccountName());
            if (byBillerID.isPresent()) {
                log.info("RETRIEVED BILLER " + byBillerID.get());
                walletAccount.setAccountName(byBillerID.get().getBiller());
                WalletAccount save = walletAccountRepository.save(walletAccount);
                log.info("SAVED SPECIAL WALLET " + save);
            }
        }

    }*/
}
