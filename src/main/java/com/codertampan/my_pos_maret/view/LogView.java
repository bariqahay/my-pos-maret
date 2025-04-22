package com.codertampan.my_pos_maret.view;

import com.codertampan.my_pos_maret.entity.*;
import com.codertampan.my_pos_maret.service.LogService;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

// ✅ Tambahkan import MainLayout
import com.codertampan.my_pos_maret.view.MainLayout;

import java.util.List;

@Route(value = "logs", layout = MainLayout.class) // ✅ Set layout-nya ke MainLayout
@PageTitle("Log View")
public class LogView extends VerticalLayout {

    private final LogService logService;

    public LogView(LogService logService) {
        this.logService = logService;

        List<Object> logs = logService.getAllLogs();

        VerticalLayout gridLayout = new VerticalLayout();
        gridLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        Grid<Object> logGrid = new Grid<>();
        logGrid.setItems(logs);
        logGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        logGrid.addColumn(log -> {
            if (log instanceof AuthLog) {
                return ((AuthLog) log).getTimestamp().toString();
            } else if (log instanceof ProductLog) {
                return ((ProductLog) log).getTimestamp().toString();
            } else if (log instanceof ProductUpdateLog) {
                return ((ProductUpdateLog) log).getTimestamp().toString();
            } else if (log instanceof SalesLog) {
                return ((SalesLog) log).getTransactionDate().toString();
            } else if (log instanceof TransactionItemLog) {
                return ((TransactionItemLog) log).getTimestamp().toString();
            } else if (log instanceof TransactionModificationLog) {
                return ((TransactionModificationLog) log).getTimestamp().toString();
            }
            return null;
        }).setHeader("Timestamp").setSortable(true).setWidth("150px");

        logGrid.addColumn(log -> log.getClass().getSimpleName()).setHeader("Log Type").setSortable(true).setWidth("150px");

        logGrid.addColumn(log -> {
            if (log instanceof AuthLog) {
                AuthLog authLog = (AuthLog) log;
                return authLog.getUsername() + " - " + authLog.getAction();
            }
            return null;
        }).setHeader("AuthLog Details");

        logGrid.addColumn(log -> {
            if (log instanceof ProductLog) {
                ProductLog productLog = (ProductLog) log;
                return productLog.getProductName() + " - " + productLog.getAction();
            }
            return null;
        }).setHeader("ProductLog Details");

        logGrid.addColumn(log -> {
            if (log instanceof ProductUpdateLog) {
                ProductUpdateLog productUpdateLog = (ProductUpdateLog) log;
                return productUpdateLog.getProductCode() + " - " + productUpdateLog.getFieldChanged() +
                        " (Old: " + productUpdateLog.getOldValue() + ", New: " + productUpdateLog.getNewValue() + ")";
            }
            return null;
        }).setHeader("ProductUpdateLog Details");

        logGrid.addColumn(log -> {
            if (log instanceof SalesLog) {
                SalesLog salesLog = (SalesLog) log;
                return salesLog.getProductName() + " - " + salesLog.getQuantity() + " pcs - " + salesLog.getTotalAmount() + " IDR";
            }
            return null;
        }).setHeader("SalesLog Details");

        logGrid.addColumn(log -> {
            if (log instanceof TransactionItemLog) {
                TransactionItemLog transactionItemLog = (TransactionItemLog) log;
                return transactionItemLog.getActionType() + " - " + transactionItemLog.getLogDetails();
            }
            return null;
        }).setHeader("TransactionItemLog Details");

        logGrid.addColumn(log -> {
            if (log instanceof TransactionModificationLog) {
                TransactionModificationLog transactionModificationLog = (TransactionModificationLog) log;
                return transactionModificationLog.getActionType() + " - " + transactionModificationLog.getDetail();
            }
            return null;
        }).setHeader("TransactionModificationLog Details");

        gridLayout.add(logGrid);
        add(gridLayout);
    }
}
