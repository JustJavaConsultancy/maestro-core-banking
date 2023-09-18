package ng.com.systemspecs.apigateway.service.impl;

import ng.com.systemspecs.apigateway.domain.Bank;
import ng.com.systemspecs.apigateway.repository.BankRepository;
import ng.com.systemspecs.apigateway.service.BankService;
import ng.com.systemspecs.apigateway.service.RITSService;
import ng.com.systemspecs.apigateway.service.dto.BankDTO;
import ng.com.systemspecs.apigateway.service.mapper.BankMapper;
import ng.com.systemspecs.remitarits.accountenquiry.AccountEnqiryRequest;
import ng.com.systemspecs.remitarits.accountenquiry.AccountEnquiryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class BankServiceImpl implements BankService {

    private final BankRepository bankRepository;
    private final BankMapper bankMapper;
    private final RITSService rITSService;

    public BankServiceImpl(BankRepository bankRepository, BankMapper bankMapper,
    		RITSService rITSService) {
        this.rITSService = rITSService;
        this.bankRepository = bankRepository;
        this.bankMapper = bankMapper;
    }

    @Override
    public BankDTO save(BankDTO bankDTO) {
        Bank save = bankRepository.save(bankMapper.toEntity(bankDTO));
        return bankMapper.toDto(save);
    }

    @Override
    public Bank save(Bank bank) {
        return bankRepository.save(bank);
    }

    @Override
    public List<BankDTO> findAll() {
        return bankRepository.findAll().stream().map(bankMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public Optional<BankDTO> findOne(Long id) {
        return bankRepository.findById(id).map(bankMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        bankRepository.deleteById(id);
    }

    @Override
    public Optional<Bank> findByBankCode(String bankCode) {
        return bankRepository.findByBankCode(bankCode);
    }

    @Override
    public Page<BankDTO> findAll(Pageable pageable) {
        return bankRepository.findAllByOrderByBankName(pageable).map(bankMapper::toDto);
    }

    @Override
    public Page<BankDTO> getAllActiveBanks(Pageable pageable) {
        return bankRepository.findAllByBankCodeIsNotOrderByBankName("000", pageable).map(bankMapper::toDto);
    }

    @Override
    public List<BankDTO> getAllActiveBanks() {
        return bankRepository.findAllByBankCodeIsNotOrderByBankName("000").stream().map(bankMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public Page<BankDTO> findAllRegularBanks(Pageable pageable) {
        return bankRepository.findAllByTypeAndBankCodeIsNotOrderByBankName("DMB", "000", pageable).map(bankMapper::toDto);
    }

    @Override
    public List<BankDTO> findCommercialBanks() {
        return bankRepository.findAllByTypeAndBankCodeIsNotOrderByBankName("DMB", "000").stream().map(bankMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<BankDTO> findMicrofinanceBanks() {
        return bankRepository.findAllByTypeAndBankCodeIsNotOrderByBankName("MFB", "000").stream().map(bankMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public AccountEnquiryResponse verifyBankAccount(String bankCode, String accountNumber) {
        AccountEnqiryRequest accountEnqiryRequest = new AccountEnqiryRequest();
        accountEnqiryRequest.setAccountNo(accountNumber);
        accountEnqiryRequest.setBankCode(bankCode);
        AccountEnquiryResponse accountEnquiryResponse = rITSService.getAccountEnquiry(accountEnqiryRequest);
        return accountEnquiryResponse;
    }

    @Override
    public Page<Bank> findAllByShortCodeIsNotNull(Pageable pageable) {
        return bankRepository.findAllByShortCodeIsNotNullOrderByBankName(pageable);
    }

    @Override
    public List<BankDTO> getValidBanks(List<BankDTO> bankList, String accountNumber) {
        List<BankDTO> validList = new ArrayList<BankDTO>();
        for (BankDTO bank : bankList) {
            System.out.println("Bank ===" + bank.getBankName());

            if (isValidNUBAN(bank.getBankCode().trim() + accountNumber.trim())) {
                validList.add(bank);
            }

		}
		return validList;
	}

	public static boolean isValidNUBAN(String accountNumber) {
		accountNumber = accountNumber.trim();

        System.out.println("NUBAN BANK NO: " + accountNumber);

		if (accountNumber.length() != 13)
			return false; // 3-digit bank code + 10-digit NUBAN

		int[] accountNumberDigits = accountNumber.chars().map(x -> x - '0').toArray();

		int sum = (accountNumberDigits[0] * 3) + (accountNumberDigits[1] * 7) + (accountNumberDigits[2] * 3)
				+ (accountNumberDigits[3] * 3) + (accountNumberDigits[4] * 7) + (accountNumberDigits[5] * 3)
				+ (accountNumberDigits[6] * 3) + (accountNumberDigits[7] * 7) + (accountNumberDigits[8] * 3)
				+ (accountNumberDigits[9] * 3) + (accountNumberDigits[10] * 7) + (accountNumberDigits[11] * 3);

		int mod = sum % 10;
		int checkDigit = (mod == 0) ? mod : 10 - mod;

		return checkDigit == accountNumberDigits[12];
	}

    public static void main(String[] args) {

    }

}
