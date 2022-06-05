package com.mvc.fraudmanagement.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.mvc.fraudmanagement.entities.Transaction;
//import com.mvc.fraudmanagement.entities.User;
import com.mvc.fraudmanagement.repos.TransactionRepository;
import com.mvc.fraudmanagement.services.TransactionService;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

@Controller
@SessionAttributes({ "transaction", "user" })
public class TransactionFraudsController {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private TransactionService transactionService;

    @RequestMapping(value = "/transaction-fraud", method = RequestMethod.GET)
    public String showTransaction() {
        return "fraud-pages/transaction-fraud";
    }

    @RequestMapping(value = "/transaction-registration", method = RequestMethod.GET)
    public String showTransactionRegistration(ModelMap model) {
        model.addAttribute("transaction", new Transaction());
        return "registration-forms/transaction-registration";
    }

    @RequestMapping(value = "/transaction-registration", method = RequestMethod.POST)
    public String saveTransactionRegistration(ModelMap model, @Valid Transaction transaction, BindingResult result) {

        if (result.hasErrors()) {
            return "registration-forms/transaction-registration";
        }

        transactionRepository.save(transaction);

        return "redirect:/transaction-fraud";
    }

    @RequestMapping(value = "/transaction-show", method = RequestMethod.GET)
    public String viewAllTransaction(ModelMap model, @RequestParam String id) {
        List<Transaction> getTransaction = transactionService.getTransactionByUserIdList(id);
        if (getTransaction.size() != 0) {
            model.put("transaction", getTransaction);
        } else {
            model.put("errorMessage", "Transaction does not exist");
        }
        return "dashboards/transaction-dashboard";
    }

    @RequestMapping(value = "/transaction-delete", method = RequestMethod.GET)
    public String deleteTransaction(@RequestParam String id) {
        Transaction transaction = transactionService.deleteTransaction(id);
        transactionRepository.delete(transaction);
        return "redirect:/transaction-fraud";
    }

    @RequestMapping(value = "/transaction-updateForm", method = RequestMethod.GET)
    public String showUpdateTransaction(ModelMap model, @RequestParam String id) {
        // Transaction transaction=transactionService.deleteTransaction(id);
        // transactionRepository.delete(transaction);
        Transaction transaction = transactionService.getTransactionByUserId(id);
        model.put("transaction", transaction);
        return "pages/transaction-updateForm";
    }

    @RequestMapping(value = "/transaction-updateForm", method = RequestMethod.POST)
    public String saveUpdateTransaction(ModelMap model, @Valid Transaction transaction, BindingResult result) {
        List<Transaction> transactionList = transactionService.getTransactionByUserIdList(transaction.getUserId());
        if (result.hasErrors()) {
            model.put("error", "field cannot be updated");
        }
        transactionRepository.save(transaction);
        model.put("transaction", transactionList);
        return "dashboards/transaction-dashboard";
    }
    
    @RequestMapping(value = "/log-out", method = RequestMethod.GET)
	public String logOutOfficer(SessionStatus sessionStatus) {
		sessionStatus.setComplete();
		return "home";
	}
}