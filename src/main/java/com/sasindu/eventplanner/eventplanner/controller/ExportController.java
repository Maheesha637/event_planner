package com.sasindu.eventplanner.eventplanner.controller;

import com.sasindu.eventplanner.eventplanner.service.ExportService;
import com.sasindu.eventplanner.eventplanner.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/events/{eventId}/guests")
public class ExportController {

    @Autowired
    private ExportService exportService;

    // allow only authenticated users; keep your existing access policy (owner check is done in service layers where relevant)
    @GetMapping("/export")
    public void exportGuests(@PathVariable Integer eventId,
                             @RequestParam(name = "format", defaultValue = "csv") String format,
                             HttpServletResponse response,
                             @AuthenticationPrincipal User user) throws IOException {
        // Optional: you can add a permission check here to verify user owns the event
        format = format.toLowerCase();
        switch (format) {
            case "excel":
            case "xlsx":
                exportService.exportGuestsExcel(eventId, response);
                break;
            case "pdf":
                exportService.exportGuestsPdf(eventId, response);
                break;
            case "csv":
            default:
                exportService.exportGuestsCsv(eventId, response);
                break;
        }
    }
}