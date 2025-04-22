package org.medirec.frontend.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class CalendarController {
    @FXML private HBox dayHeaderRow;
    @FXML private GridPane calendarGrid;
    @FXML private Label monthLabel;
    @FXML private Button prevMonthButton, nextMonthButton, newAppointmentButton;
    @FXML private ToggleButton dayViewButton, weekViewButton, monthViewButton;
    @FXML private ScrollPane calendarScrollPane;

    private YearMonth currentYearMonth;
    private final Locale SK_LOCALE = new Locale("sk");
    private final Map<LocalDate, List<Appointment>> appointments = new HashMap<>();



    public void initialize() {
        currentYearMonth = YearMonth.now();
        setupDayHeaderRow();
        setupGridLayout();
        updateCalendar();
        updateMonthLabel();
    }

    private void setupDayHeaderRow() {
        ResourceBundle bundle = ResourceBundle.getBundle("messages", AppSettings.getLocale());
        String[] daysSk = {
                bundle.getString("calendar.day.mo"),
                bundle.getString("calendar.day.tu"),
                bundle.getString("calendar.day.we"),
                bundle.getString("calendar.day.th"),
                bundle.getString("calendar.day.fr"),
                bundle.getString("calendar.day.sa"),
                bundle.getString("calendar.day.su")
        };

        dayHeaderRow.getChildren().clear();

        for (String day : daysSk) {
            Label label = new Label(day);
            label.getStyleClass().add("day-header");
            label.setMaxWidth(Double.MAX_VALUE);
            label.setAlignment(Pos.CENTER);
            HBox.setHgrow(label, Priority.ALWAYS);
            dayHeaderRow.getChildren().add(label);
        }
    }

    private void updateCalendar() {
        calendarGrid.getChildren().clear();

        LocalDate firstOfMonth = currentYearMonth.atDay(1);
        int startDay = firstOfMonth.getDayOfWeek().getValue();
        int daysInMonth = currentYearMonth.lengthOfMonth();

        int totalSlots = daysInMonth + (startDay - 1);
        int totalRows = (int) Math.ceil(totalSlots / 7.0);

        setupGridLayout(totalRows);
        int dayCounter = 1;

        for (int row = 0; row < totalRows; row++) {
            for (int col = 0; col < 7; col++) {
                StackPane cell = new StackPane();
                cell.getStyleClass().add("calendar-cell");

                if ((row == 0 && col + 1 < startDay) || dayCounter > daysInMonth) {
                    calendarGrid.add(cell, col, row);
                    continue;
                }

                Label dayLabel = new Label(String.valueOf(dayCounter));
                dayLabel.getStyleClass().add("day-label");

                VBox contentBox = new VBox(3);
                contentBox.setAlignment(Pos.TOP_CENTER);
                contentBox.getChildren().add(dayLabel);

                LocalDate cellDate = currentYearMonth.atDay(dayCounter);
                List<Appointment> dayAppointments = appointments.getOrDefault(cellDate, new ArrayList<>());

                int visibleCount = 0;
                LocalDateTime now = LocalDateTime.now();

                for (Appointment app : dayAppointments) {
                    try {
                        LocalTime appTime = LocalTime.parse(app.getTime());
                        LocalDateTime appDateTime = LocalDateTime.of(app.getDate(), appTime);
                        if (appDateTime.plusMinutes(20).isBefore(now)) {
                            continue; // skip expired
                        }
                    } catch (DateTimeParseException e) {
                        continue;
                    }

                    if (visibleCount >= 2) break;

                    Label label = new Label(app.getTime() + " - " + app.getType());
                    label.getStyleClass().add("appt-label");
                    label.setStyle("-fx-text-fill: " + getColorByType(app.getType()) + ";");
                    label.setOnMouseClicked(e -> showAppointmentDetailPopup(app, label));
                    contentBox.getChildren().add(label);
                    visibleCount++;
                }

                long remaining = dayAppointments.stream()
                        .filter(app -> {
                            try {
                                LocalTime appTime = LocalTime.parse(app.getTime());
                                LocalDateTime appDateTime = LocalDateTime.of(app.getDate(), appTime);
                                return !appDateTime.plusMinutes(20).isBefore(now);
                            } catch (DateTimeParseException e) {
                                return false;
                            }
                        })
                        .count();

                if (remaining > 2) {
                    ResourceBundle bundle = ResourceBundle.getBundle("messages", AppSettings.getLocale());
                    String text = MessageFormat.format(bundle.getString("calendar.more.label"), (remaining - 2));
                    Label moreLabel = new Label(text);

                    moreLabel.setStyle("-fx-text-fill: #95a5a6; -fx-font-size: 11px;");
                    moreLabel.setOnMouseClicked(e -> showAppointmentPopup(cellDate));
                    contentBox.getChildren().add(moreLabel);
                }

                cell.getChildren().add(contentBox);
                cell.setUserData(cellDate);

                GridPane.setHgrow(cell, Priority.ALWAYS);
                GridPane.setVgrow(cell, Priority.ALWAYS);
                calendarGrid.add(cell, col, row);
                dayCounter++;
            }
        }
    }
    private void setupGridLayout() {
        calendarGrid.getColumnConstraints().clear();
        for (int i = 0; i < 7; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(100.0 / 7);
            col.setHgrow(Priority.ALWAYS);
            calendarGrid.getColumnConstraints().add(col);
        }
        calendarGrid.setGridLinesVisible(false);
    }

    private void setupGridLayout(int totalRows) {
        setupGridLayout();
        calendarGrid.getRowConstraints().clear();
        for (int i = 0; i < totalRows; i++) {
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(100.0 / totalRows);
            row.setVgrow(Priority.ALWAYS);
            calendarGrid.getRowConstraints().add(row);
        }
    }

    private void updateMonthLabel() {
        Locale locale = AppSettings.getLocale(); // získa aktuálne zvolený jazyk
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("LLLL yyyy", locale);
        String formatted = currentYearMonth.atDay(1).format(formatter);
        monthLabel.setText(capitalize(formatted));
    }


    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public void addAppointment(LocalDate date, String type, String time) {
        List<Appointment> dayAppointments = appointments.computeIfAbsent(date, d -> new ArrayList<>());

        // 🔒 Overenie: či už existuje stretnutie na rovnaký čas
        boolean alreadyExists = dayAppointments.stream()
                .anyMatch(app -> app.getTime().equals(time));
        LocalTime now = LocalTime.now();
        LocalDate today=LocalDate.now();
        LocalTime planed= LocalTime.parse(time);
        ResourceBundle bundle = ResourceBundle.getBundle("messages", AppSettings.getLocale());

        if (alreadyExists) {
            showAlert(
                    bundle.getString("calendar.error.duplicate.title"),
                    bundle.getString("calendar.error.duplicate.header"),
                    bundle.getString("calendar.error.duplicate.content").replace("{time}", time)
            );
            return;
        } else if (planed.isBefore(now) && today.equals(date)) {
            showAlert(
                    bundle.getString("calendar.error.pasttime.title"),
                    bundle.getString("calendar.error.pasttime.header"),
                    bundle.getString("calendar.error.pasttime.content")
            );
            return;
        } else if (today.isAfter(date)) {
            showAlert(
                    bundle.getString("calendar.error.pastdate.title"),
                    bundle.getString("calendar.error.pastdate.header"),
                    bundle.getString("calendar.error.pastdate.content")
            );
            return;
        }


        // ✅ Ak neexistuje — pridaj
        dayAppointments.add(new Appointment(date, type, time));
        updateCalendar();
    }


    public void removeAppointment(Appointment appointment) {
        List<Appointment> list = appointments.get(appointment.getDate());
        if (list != null) {
            list.removeIf(a -> a.getTime().equals(appointment.getTime()) && a.getType().equals(appointment.getType()));
            if (list.isEmpty()) {
                appointments.remove(appointment.getDate());
            }
        }
        updateCalendar();
    }

    public String getColorByType(String localizedType) {
        ResourceBundle bundle = ResourceBundle.getBundle("messages", AppSettings.getLocale());

        if (localizedType.equals(bundle.getString("appointment.type.consultation"))) {
            return bundle.getString("appointment.color.consultation");
        } else if (localizedType.equals(bundle.getString("appointment.type.urgent"))) {
            return bundle.getString("appointment.color.urgent");
        } else if (localizedType.equals(bundle.getString("appointment.type.examination"))) {
            return bundle.getString("appointment.color.examination");
        } else {
            return "#7f8c8d"; // default farba
        }
    }


    @FXML
    private void openAppointmentPopup() {
        try {
// Získaj lokalizačný ResourceBundle podľa aktuálneho jazyka
            ResourceBundle bundle = ResourceBundle.getBundle("messages", AppSettings.getLocale());

// Načítaj FXML s lokalizáciou
            FXMLLoader loader = new FXMLLoader(getClass().getResource("appointment_popup.fxml"), bundle);
            Scene scene = new Scene(loader.load());

// Prepoj controller, ak potrebuješ referenciu
            AppointmentPopupController controller = loader.getController();
            controller.setCalendarController(this);

// Vytvor popup okno
            Stage popupStage = new Stage();
            popupStage.setTitle(bundle.getString("appointment.popup.title")); // z properties
            popupStage.setScene(scene);
            popupStage.setResizable(false);
            popupStage.initOwner(newAppointmentButton.getScene().getWindow());
            popupStage.showAndWait();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void showAppointmentPopup(LocalDate date) {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("messages", AppSettings.getLocale());

            FXMLLoader loader = new FXMLLoader(getClass().getResource("appointments_by_day_popup.fxml"), bundle);
            Scene scene = new Scene(loader.load());

            AppointmentsByDayController controller = loader.getController();
            controller.setDate(date);

            List<Appointment> dailyAppointments = appointments.getOrDefault(date, Collections.emptyList());
            controller.setAppointments(dailyAppointments);

            Stage popup = new Stage();
            popup.setTitle(bundle.getString("appointment.day.title") + " - " + date);
            popup.setScene(scene);
            popup.setResizable(false);
            popup.initOwner(calendarGrid.getScene().getWindow());
            popup.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void showAppointmentDetailPopup(Appointment app, Labeled label) {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("messages", AppSettings.getLocale());

            FXMLLoader loader = new FXMLLoader(getClass().getResource("appointment_detail_popup.fxml"), bundle);
            Scene scene = new Scene(loader.load());

            AppointmentDetailController controller = loader.getController();
            controller.setData(app, this);

            Stage popup = new Stage();
            popup.setTitle(bundle.getString("appointment.detail.title") + " - " + app.getDate());
            popup.setScene(scene);
            popup.setResizable(false);
            popup.initOwner(calendarGrid.getScene().getWindow());
            popup.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handlePrevMonth() {
        currentYearMonth = currentYearMonth.minusMonths(1);
        updateCalendar();
        updateMonthLabel();
    }

    @FXML
    private void handleNextMonth() {
        currentYearMonth = currentYearMonth.plusMonths(1);
        updateCalendar();
        updateMonthLabel();
    }

    private void showAlert(String title,String header,String msg){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(msg);
        alert.showAndWait();
        return;
    }


    @FXML
    private void handleMonth() {
        //neviem co presne treba
    }
    @FXML
    private void handleDay() {
        //neviem co presne treba
    }
    @FXML
    private void handleWeek() {
        //neviem co presne treba
    }

}
