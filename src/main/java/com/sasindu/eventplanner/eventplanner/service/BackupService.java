package com.sasindu.eventplanner.eventplanner.service;

import com.sasindu.eventplanner.eventplanner.model.*;
import com.sasindu.eventplanner.eventplanner.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class BackupService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private GuestRepository guestRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private GlobalVendorRepository globalVendorRepository;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ISO_LOCAL_DATE;

    public void streamFullDatabaseBackup(HttpServletResponse response) throws IOException {
        String filename = "eventplanner-backup-" + java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + ".zip";

        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

        try (OutputStream out = response.getOutputStream();
             ZipOutputStream zos = new ZipOutputStream(out)) {

            writeUsersCsv(zos);
            writeEventsCsv(zos);
            writeGuestsCsv(zos);
            writeTasksCsv(zos);
            writeVendorsCsv(zos);
            writeContractsCsv(zos);
            writeBudgetsCsv(zos);
            writeExpensesCsv(zos);
            writeGlobalVendorsCsv(zos);

            zos.finish();
            zos.flush();
        }
    }

    private void writeUsersCsv(ZipOutputStream zos) throws IOException {
        List<User> users = userRepository.findAll();
        zos.putNextEntry(new ZipEntry("users.csv"));
        StringBuilder sb = new StringBuilder();
        sb.append("UserID,FirstName,LastName,Email,Password\n");
        for (User u : users) {
            sb.append(csv(u.getUserID() != null ? u.getUserID().toString() : ""));
            sb.append(",");
            sb.append(csv(u.getFirstName()));
            sb.append(",");
            sb.append(csv(u.getLastName()));
            sb.append(",");
            sb.append(csv(u.getEmail()));
            sb.append(",");
            sb.append(csv(u.getPassword()));
            sb.append("\n");
        }
        zos.write(sb.toString().getBytes(StandardCharsets.UTF_8));
        zos.closeEntry();
    }

    private void writeEventsCsv(ZipOutputStream zos) throws IOException {
        List<Event> events = eventRepository.findAll();
        zos.putNextEntry(new ZipEntry("events.csv"));
        StringBuilder sb = new StringBuilder();
        sb.append("EventID,Name,Date,Type,Status,Location,UserID\n");
        for (Event e : events) {
            sb.append(csv(e.getEventID() != null ? e.getEventID().toString() : ""));
            sb.append(",");
            sb.append(csv(e.getName()));
            sb.append(",");
            sb.append(csv(e.getDate() != null ? e.getDate().format(DATE_FMT) : ""));
            sb.append(",");
            sb.append(csv(e.getType()));
            sb.append(",");
            sb.append(csv(e.getStatus()));
            sb.append(",");
            sb.append(csv(e.getLocation()));
            sb.append(",");
            sb.append(csv(e.getUser() != null && e.getUser().getUserID() != null ? e.getUser().getUserID().toString() : ""));
            sb.append("\n");
        }
        zos.write(sb.toString().getBytes(StandardCharsets.UTF_8));
        zos.closeEntry();
    }

    private void writeGuestsCsv(ZipOutputStream zos) throws IOException {
        List<Guest> guests = guestRepository.findAll();
        zos.putNextEntry(new ZipEntry("guests.csv"));
        StringBuilder sb = new StringBuilder();
        sb.append("GuestID,FirstName,LastName,ContactDetails,EventID\n");
        for (Guest g : guests) {
            sb.append(csv(g.getGuestID() != null ? g.getGuestID().toString() : ""));
            sb.append(",");
            sb.append(csv(g.getFirstName()));
            sb.append(",");
            sb.append(csv(g.getLastName()));
            sb.append(",");
            sb.append(csv(g.getContactDetails()));
            sb.append(",");
            sb.append(csv(g.getEvent() != null && g.getEvent().getEventID() != null ? g.getEvent().getEventID().toString() : ""));
            sb.append("\n");
        }
        zos.write(sb.toString().getBytes(StandardCharsets.UTF_8));
        zos.closeEntry();
    }

    private void writeTasksCsv(ZipOutputStream zos) throws IOException {
        List<Task> tasks = taskRepository.findAll();
        zos.putNextEntry(new ZipEntry("tasks.csv"));
        StringBuilder sb = new StringBuilder();
        sb.append("TaskID,Description,Deadline,Status,EventID\n");
        for (Task t : tasks) {
            sb.append(csv(t.getTaskID() != null ? t.getTaskID().toString() : ""));
            sb.append(",");
            sb.append(csv(t.getDescription()));
            sb.append(",");
            sb.append(csv(t.getDeadline() != null ? t.getDeadline().format(DATE_FMT) : ""));
            sb.append(",");
            sb.append(csv(t.getStatus()));
            sb.append(",");
            sb.append(csv(t.getEvent() != null && t.getEvent().getEventID() != null ? t.getEvent().getEventID().toString() : ""));
            sb.append("\n");
        }
        zos.write(sb.toString().getBytes(StandardCharsets.UTF_8));
        zos.closeEntry();
    }

    private void writeVendorsCsv(ZipOutputStream zos) throws IOException {
        List<Vendor> vendors = vendorRepository.findAll();
        zos.putNextEntry(new ZipEntry("vendors.csv"));
        StringBuilder sb = new StringBuilder();
        sb.append("VendorID,Name,Category,Description,ContactDetails,Rating,UserID,EventID\n");
        for (Vendor v : vendors) {
            sb.append(csv(v.getVendorID() != null ? v.getVendorID().toString() : ""));
            sb.append(",");
            sb.append(csv(v.getName()));
            sb.append(",");
            sb.append(csv(v.getCategory()));
            sb.append(",");
            sb.append(csv(v.getDescription()));
            sb.append(",");
            sb.append(csv(v.getContactDetails()));
            sb.append(",");
            sb.append(csv(v.getRating() != null ? v.getRating().toString() : ""));
            sb.append(",");
            sb.append(csv(v.getUser() != null && v.getUser().getUserID() != null ? v.getUser().getUserID().toString() : ""));
            sb.append(",");
            sb.append(csv(v.getEvent() != null && v.getEvent().getEventID() != null ? v.getEvent().getEventID().toString() : ""));
            sb.append("\n");
        }
        zos.write(sb.toString().getBytes(StandardCharsets.UTF_8));
        zos.closeEntry();
    }

    private void writeContractsCsv(ZipOutputStream zos) throws IOException {
        List<Contract> contracts = contractRepository.findAll();
        zos.putNextEntry(new ZipEntry("contracts.csv"));
        StringBuilder sb = new StringBuilder();
        sb.append("ContractID,Attribute,PaymentAmount,PaymentStatus,VendorID\n");
        for (Contract c : contracts) {
            sb.append(csv(c.getContractID() != null ? c.getContractID().toString() : ""));
            sb.append(",");
            sb.append(csv(c.getAttribute()));
            sb.append(",");
            sb.append(csv(c.getPaymentAmount() != null ? c.getPaymentAmount().toString() : ""));
            sb.append(",");
            sb.append(csv(c.getPaymentStatus()));
            sb.append(",");
            sb.append(csv(c.getVendor() != null && c.getVendor().getVendorID() != null ? c.getVendor().getVendorID().toString() : ""));
            sb.append("\n");
        }
        zos.write(sb.toString().getBytes(StandardCharsets.UTF_8));
        zos.closeEntry();
    }

    private void writeBudgetsCsv(ZipOutputStream zos) throws IOException {
        List<Budget> budgets = budgetRepository.findAll();
        zos.putNextEntry(new ZipEntry("budgets.csv"));
        StringBuilder sb = new StringBuilder();
        sb.append("BudgetID,Theme,TotalBudget,Achievement,EventID\n");
        for (Budget b : budgets) {
            sb.append(csv(b.getBudgetID() != null ? b.getBudgetID().toString() : ""));
            sb.append(",");
            sb.append(csv(b.getTheme()));
            sb.append(",");
            sb.append(csv(b.getTotalBudget() != null ? b.getTotalBudget().toString() : ""));
            sb.append(",");
            sb.append(csv(b.getAchievement() != null ? b.getAchievement().toString() : ""));
            sb.append(",");
            sb.append(csv(b.getEvent() != null && b.getEvent().getEventID() != null ? b.getEvent().getEventID().toString() : ""));
            sb.append("\n");
        }
        zos.write(sb.toString().getBytes(StandardCharsets.UTF_8));
        zos.closeEntry();
    }

    private void writeExpensesCsv(ZipOutputStream zos) throws IOException {
        List<Expense> expenses = expenseRepository.findAll();
        zos.putNextEntry(new ZipEntry("expenses.csv"));
        StringBuilder sb = new StringBuilder();
        sb.append("ExpenseID,Description,Amount,Date,BudgetID\n");
        for (Expense e : expenses) {
            sb.append(csv(e.getExpenseID() != null ? e.getExpenseID().toString() : ""));
            sb.append(",");
            sb.append(csv(e.getDescription()));
            sb.append(",");
            sb.append(csv(e.getAmount() != null ? e.getAmount().toString() : ""));
            sb.append(",");
            sb.append(csv(e.getDate() != null ? e.getDate().format(DATE_FMT) : ""));
            sb.append(",");
            sb.append(csv(e.getBudget() != null && e.getBudget().getBudgetID() != null ? e.getBudget().getBudgetID().toString() : ""));
            sb.append("\n");
        }
        zos.write(sb.toString().getBytes(StandardCharsets.UTF_8));
        zos.closeEntry();
    }

    private void writeGlobalVendorsCsv(ZipOutputStream zos) throws IOException {
        List<GlobalVendor> gvs = globalVendorRepository.findAll();
        zos.putNextEntry(new ZipEntry("global_vendors.csv"));
        StringBuilder sb = new StringBuilder();
        sb.append("GlobalVendorID,Name,Category,Description,ContactDetails,Rating\n");
        for (GlobalVendor g : gvs) {
            sb.append(csv(g.getGlobalVendorID() != null ? g.getGlobalVendorID().toString() : ""));
            sb.append(",");
            sb.append(csv(g.getName()));
            sb.append(",");
            sb.append(csv(g.getCategory()));
            sb.append(",");
            sb.append(csv(g.getDescription()));
            sb.append(",");
            sb.append(csv(g.getContactDetails()));
            sb.append(",");
            sb.append(csv(g.getRating() != null ? g.getRating().toString() : ""));
            sb.append("\n");
        }
        zos.write(sb.toString().getBytes(StandardCharsets.UTF_8));
        zos.closeEntry();
    }

    private String csv(String s) {
        if (s == null) return "";
        String out = s.replace("\"", "\"\"");
        if (out.contains(",") || out.contains("\"") || out.contains("\n") || out.contains("\r")) {
            return "\"" + out + "\"";
        }
        return out;
    }
}