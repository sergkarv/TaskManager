package taskmanagerclient;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import connection.ConnectionClass;
import sound.Sound;
import tableModel.DefaultTableModelNotEdit;
import task.Task;

public class GUI extends javax.swing.JFrame {
    /* Подключение к серверу */

    public ConnectionClass connection;
    /* Данные о порте и адресе */
    private String port;
    private String address;
    private String nameUser;
    /* Рендерер для таблицы, красные - высокоприоритетные задачи */
    private final TableCellRenderer renderer;
    /**
     * Объект для работы со звуком
     */
    private Sound sound = new Sound();
    private String pathSound = "signal/bell.wav";

    public GUI() {
        initComponents();
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+4"));
        //Вызываем функцию подключения к серверу
        connect();
        renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table,
                    Object value, boolean isSelected, boolean hasFocus,
                    int row, int column) {
                Component cell = super.getTableCellRendererComponent(table,
                        value, hasFocus, hasFocus, row, column);
                long idTask = (long) table.getValueAt(row, 0);
                Task cellValue = connection.getTask(idTask);
                if (cellValue.isHighPriority()) {
                    cell.setForeground(Color.RED);
                } else {
                    cell.setForeground(Color.BLACK);
                }
                return cell;
            }
        };
        taskTable.setAutoCreateRowSorter(true);
        //Запрашиваем получение всех задач
        connection.getAllTasks(nameUser);
    }

    //Подключение к серверу
    private void connect() {
        boolean flag = true;
        while (flag) {
            //Центрируем окно ввода адреса и порта
            enterParamDialog.setLocationRelativeTo(null);
            enterParamDialog.setModal(true);
            enterParamDialog.setVisible(true);
            try {

                //И пытаемся подключиться
                connection = new ConnectionClass(
                        address, Integer.parseInt(port), nameUser, this);
                flag = false;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                        "Введены некорректные данные.", "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                        "Не удалось подключиться к удалённому серверу",
                        "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        taskDialog = new javax.swing.JFrame();
        namePanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        nameTextField = new javax.swing.JTextField();
        descriptionPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        descriptionTextArea = new javax.swing.JTextArea();
        contactsPanel = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        contactsTextArea = new javax.swing.JTextArea();
        dateTimePanel = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        dateChooser = new com.toedter.calendar.JDateChooser();
        hourSpinField = new com.toedter.components.JSpinField();
        minuteSpinField = new com.toedter.components.JSpinField();
        secondSpinField = new com.toedter.components.JSpinField();
        priorityPanel = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        normalPriorityRadioButton = new javax.swing.JRadioButton();
        highPriorityRadioButton = new javax.swing.JRadioButton();
        buttonsPanel = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        priorityButtonGroup = new javax.swing.ButtonGroup();
        enterParamDialog = new javax.swing.JDialog();
        okParamButton = new javax.swing.JButton();
        portLabel = new javax.swing.JLabel();
        addressLabel = new javax.swing.JLabel();
        portTextField = new javax.swing.JTextField();
        addressTextField = new javax.swing.JTextField();
        userLabel = new javax.swing.JLabel();
        userTextField = new javax.swing.JTextField();
        completedTasksDialog = new javax.swing.JDialog();
        completedTasksListPanel = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        completedTasksList = new javax.swing.JList();
        completedTasksInfoPanel = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        completedTaskNameTextField = new javax.swing.JTextField();
        jScrollPane5 = new javax.swing.JScrollPane();
        completedTaskDescriptionTextArea = new javax.swing.JTextArea();
        jScrollPane6 = new javax.swing.JScrollPane();
        completedTaskContactsTextArea = new javax.swing.JTextArea();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        prorogueHourSpinField = new com.toedter.components.JSpinField();
        prorogueMinuteSpinField = new com.toedter.components.JSpinField();
        prorogueSecondSpinField = new com.toedter.components.JSpinField();
        prorogueCheckBox = new javax.swing.JCheckBox();
        dateChooserRadioButton = new javax.swing.JRadioButton();
        dateComboBoxRadioButton = new javax.swing.JRadioButton();
        dateComboBox = new javax.swing.JComboBox();
        prorogueButton = new javax.swing.JButton();
        deleteButton1 = new javax.swing.JButton();
        finishButton = new javax.swing.JButton();
        datebuttonGroup = new javax.swing.ButtonGroup();
        selectAlertFileChooser = new javax.swing.JFileChooser();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        taskTable = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        addButton = new javax.swing.JButton();
        editButton = new javax.swing.JButton();
        viewButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        openCompleteTaskDialogButton = new javax.swing.JButton();
        changeSoundAlertButton = new javax.swing.JButton();

        taskDialog.setLocationByPlatform(true);
        taskDialog.setMinimumSize(new java.awt.Dimension(400, 450));
        taskDialog.setResizable(false);

        jLabel1.setText("Название:");

        javax.swing.GroupLayout namePanelLayout = new javax.swing.GroupLayout(namePanel);
        namePanel.setLayout(namePanelLayout);
        namePanelLayout.setHorizontalGroup(
            namePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(namePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(nameTextField)
                .addContainerGap())
        );
        namePanelLayout.setVerticalGroup(
            namePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(namePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(namePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel2.setText("Описание:");

        descriptionTextArea.setColumns(20);
        descriptionTextArea.setRows(5);
        jScrollPane2.setViewportView(descriptionTextArea);

        javax.swing.GroupLayout descriptionPanelLayout = new javax.swing.GroupLayout(descriptionPanel);
        descriptionPanel.setLayout(descriptionPanelLayout);
        descriptionPanelLayout.setHorizontalGroup(
            descriptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(descriptionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        descriptionPanelLayout.setVerticalGroup(
            descriptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(descriptionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, descriptionPanelLayout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel3.setText("Контакты:");

        contactsTextArea.setColumns(20);
        contactsTextArea.setRows(5);
        jScrollPane3.setViewportView(contactsTextArea);

        javax.swing.GroupLayout contactsPanelLayout = new javax.swing.GroupLayout(contactsPanel);
        contactsPanel.setLayout(contactsPanelLayout);
        contactsPanelLayout.setHorizontalGroup(
            contactsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contactsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        contactsPanelLayout.setVerticalGroup(
            contactsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contactsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, contactsPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jLabel4.setText("Дата/время:");

        hourSpinField.setMaximum(23);
        hourSpinField.setMinimum(0);

        minuteSpinField.setMaximum(59);
        minuteSpinField.setMinimum(0);

        secondSpinField.setMaximum(59);
        secondSpinField.setMinimum(0);

        javax.swing.GroupLayout dateTimePanelLayout = new javax.swing.GroupLayout(dateTimePanel);
        dateTimePanel.setLayout(dateTimePanelLayout);
        dateTimePanelLayout.setHorizontalGroup(
            dateTimePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dateTimePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(dateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(hourSpinField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(minuteSpinField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(secondSpinField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        dateTimePanelLayout.setVerticalGroup(
            dateTimePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dateTimePanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(dateTimePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(secondSpinField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(minuteSpinField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(hourSpinField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addContainerGap())
        );

        jLabel5.setText("Приоритет:");

        priorityButtonGroup.add(normalPriorityRadioButton);
        normalPriorityRadioButton.setText("Обычный");

        priorityButtonGroup.add(highPriorityRadioButton);
        highPriorityRadioButton.setText("Высокий");

        javax.swing.GroupLayout priorityPanelLayout = new javax.swing.GroupLayout(priorityPanel);
        priorityPanel.setLayout(priorityPanelLayout);
        priorityPanelLayout.setHorizontalGroup(
            priorityPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(priorityPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addComponent(normalPriorityRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(highPriorityRadioButton)
                .addGap(60, 60, 60))
        );
        priorityPanelLayout.setVerticalGroup(
            priorityPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(priorityPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(priorityPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(normalPriorityRadioButton)
                    .addComponent(highPriorityRadioButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Отмена");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout buttonsPanelLayout = new javax.swing.GroupLayout(buttonsPanel);
        buttonsPanel.setLayout(buttonsPanelLayout);
        buttonsPanelLayout.setHorizontalGroup(
            buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttonsPanelLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37))
        );
        buttonsPanelLayout.setVerticalGroup(
            buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttonsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout taskDialogLayout = new javax.swing.GroupLayout(taskDialog.getContentPane());
        taskDialog.getContentPane().setLayout(taskDialogLayout);
        taskDialogLayout.setHorizontalGroup(
            taskDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, taskDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(taskDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(priorityPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(buttonsPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dateTimePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(contactsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(descriptionPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(namePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        taskDialogLayout.setVerticalGroup(
            taskDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(taskDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(namePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(descriptionPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(contactsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dateTimePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(priorityPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        enterParamDialog.setTitle("Соединение с сервером");
        enterParamDialog.setMinimumSize(new java.awt.Dimension(320, 220));
        enterParamDialog.setModal(true);
        enterParamDialog.setResizable(false);
        enterParamDialog.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                enterParamDialogWindowClosing(evt);
            }
        });

        okParamButton.setText("Подключиться");
        okParamButton.setMaximumSize(new java.awt.Dimension(120, 43));
        okParamButton.setMinimumSize(new java.awt.Dimension(120, 43));
        okParamButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okParamButtonActionPerformed(evt);
            }
        });

        portLabel.setText("Порт:");

        addressLabel.setText("Адрес:");

        portTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                portTextFieldKeyPressed(evt);
            }
        });

        addressTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                addressTextFieldKeyPressed(evt);
            }
        });

        userLabel.setText("Пользователь:");

        javax.swing.GroupLayout enterParamDialogLayout = new javax.swing.GroupLayout(enterParamDialog.getContentPane());
        enterParamDialog.getContentPane().setLayout(enterParamDialogLayout);
        enterParamDialogLayout.setHorizontalGroup(
            enterParamDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(enterParamDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(enterParamDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(enterParamDialogLayout.createSequentialGroup()
                        .addGroup(enterParamDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(addressLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(portLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(enterParamDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(portTextField, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(addressTextField, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(enterParamDialogLayout.createSequentialGroup()
                        .addComponent(userLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(userTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(enterParamDialogLayout.createSequentialGroup()
                .addGap(86, 86, 86)
                .addComponent(okParamButton, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        enterParamDialogLayout.setVerticalGroup(
            enterParamDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, enterParamDialogLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(enterParamDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addressTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addressLabel))
                .addGap(26, 26, 26)
                .addGroup(enterParamDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(portLabel)
                    .addComponent(portTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(enterParamDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(userLabel)
                    .addComponent(userTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(okParamButton, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        completedTasksDialog.setTitle("Список Активных Задач");
        completedTasksDialog.setLocationByPlatform(true);
        completedTasksDialog.setMinimumSize(new java.awt.Dimension(652, 396));
        completedTasksDialog.setResizable(false);
        completedTasksDialog.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                completedTasksDialogWindowOpened(evt);
            }
        });

        completedTasksListPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Список заданий");

        completedTasksList.setModel(new DefaultListModel<Task>());
        completedTasksList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        completedTasksList.setFocusCycleRoot(true);
        completedTasksList.setVisibleRowCount(100);
        completedTasksList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                completedTasksListMousePressed(evt);
            }
        });
        jScrollPane4.setViewportView(completedTasksList);

        javax.swing.GroupLayout completedTasksListPanelLayout = new javax.swing.GroupLayout(completedTasksListPanel);
        completedTasksListPanel.setLayout(completedTasksListPanelLayout);
        completedTasksListPanelLayout.setHorizontalGroup(
            completedTasksListPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(completedTasksListPanelLayout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addComponent(jLabel6)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(completedTasksListPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4)
                .addContainerGap())
        );
        completedTasksListPanelLayout.setVerticalGroup(
            completedTasksListPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(completedTasksListPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel7.setText("Название:");

        jLabel12.setText("Описание:");

        jLabel13.setText("Контакты:");

        completedTaskDescriptionTextArea.setColumns(20);
        completedTaskDescriptionTextArea.setRows(5);
        jScrollPane5.setViewportView(completedTaskDescriptionTextArea);

        completedTaskContactsTextArea.setColumns(20);
        completedTaskContactsTextArea.setRows(5);
        jScrollPane6.setViewportView(completedTaskContactsTextArea);

        jDateChooser1.setEnabled(false);
        jDateChooser1.setCalendar(new GregorianCalendar());

        prorogueHourSpinField.setEnabled(false);
        prorogueHourSpinField.setMaximum(23);
        prorogueHourSpinField.setMinimum(0);

        prorogueMinuteSpinField.setEnabled(false);
        prorogueMinuteSpinField.setMaximum(59);
        prorogueMinuteSpinField.setMinimum(0);

        prorogueSecondSpinField.setEnabled(false);
        prorogueSecondSpinField.setMaximum(59);
        prorogueSecondSpinField.setMinimum(0);

        prorogueCheckBox.setText("Отложить:");
        prorogueCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prorogueCheckBoxActionPerformed(evt);
            }
        });

        datebuttonGroup.add(dateChooserRadioButton);
        dateChooserRadioButton.setSelected(true);
        dateChooserRadioButton.setText("на выбранную дату/время:");
        dateChooserRadioButton.setEnabled(false);
        dateChooserRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dateChooserRadioButtonActionPerformed(evt);
            }
        });

        datebuttonGroup.add(dateComboBoxRadioButton);
        dateComboBoxRadioButton.setText("на определённый промежуток:");
        dateComboBoxRadioButton.setEnabled(false);
        dateComboBoxRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dateComboBoxRadioButtonActionPerformed(evt);
            }
        });

        dateComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "на час", "на день", "на неделю", "на месяц" }));
        dateComboBox.setEnabled(false);

        prorogueButton.setText("Отложить");
        prorogueButton.setEnabled(false);
        prorogueButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prorogueButtonActionPerformed(evt);
            }
        });

        deleteButton1.setText("Удалить");
        deleteButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButton1ActionPerformed(evt);
            }
        });

        finishButton.setText("Завершить");
        finishButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                finishButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout completedTasksInfoPanelLayout = new javax.swing.GroupLayout(completedTasksInfoPanel);
        completedTasksInfoPanel.setLayout(completedTasksInfoPanelLayout);
        completedTasksInfoPanelLayout.setHorizontalGroup(
            completedTasksInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(completedTasksInfoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(completedTasksInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(completedTasksInfoPanelLayout.createSequentialGroup()
                        .addGroup(completedTasksInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(completedTasksInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(completedTaskNameTextField)
                            .addComponent(jScrollPane5)
                            .addComponent(jScrollPane6)))
                    .addGroup(completedTasksInfoPanelLayout.createSequentialGroup()
                        .addComponent(prorogueCheckBox)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, completedTasksInfoPanelLayout.createSequentialGroup()
                        .addGroup(completedTasksInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(completedTasksInfoPanelLayout.createSequentialGroup()
                                .addComponent(prorogueButton, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(39, 39, 39)
                                .addComponent(finishButton, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(deleteButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(completedTasksInfoPanelLayout.createSequentialGroup()
                                .addGap(5, 5, 5)
                                .addGroup(completedTasksInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(completedTasksInfoPanelLayout.createSequentialGroup()
                                        .addComponent(dateComboBoxRadioButton)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(dateComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(completedTasksInfoPanelLayout.createSequentialGroup()
                                        .addComponent(dateChooserRadioButton)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(prorogueHourSpinField, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(prorogueMinuteSpinField, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(prorogueSecondSpinField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(8, 8, 8)))
                .addContainerGap())
        );
        completedTasksInfoPanelLayout.setVerticalGroup(
            completedTasksInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(completedTasksInfoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(completedTasksInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(completedTaskNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(completedTasksInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(completedTasksInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                .addComponent(prorogueCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(completedTasksInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dateChooserRadioButton)
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(prorogueHourSpinField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(prorogueMinuteSpinField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(prorogueSecondSpinField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(completedTasksInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dateComboBoxRadioButton)
                    .addComponent(dateComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(52, 52, 52)
                .addGroup(completedTasksInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(prorogueButton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(deleteButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(finishButton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout completedTasksDialogLayout = new javax.swing.GroupLayout(completedTasksDialog.getContentPane());
        completedTasksDialog.getContentPane().setLayout(completedTasksDialogLayout);
        completedTasksDialogLayout.setHorizontalGroup(
            completedTasksDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(completedTasksDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(completedTasksListPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(completedTasksInfoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        completedTasksDialogLayout.setVerticalGroup(
            completedTasksDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, completedTasksDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(completedTasksDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(completedTasksInfoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(completedTasksListPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "WAV Files", "wav");
        selectAlertFileChooser.setFileFilter(filter);
        selectAlertFileChooser.setCurrentDirectory(new java.io.File("C:\\"));

            setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
            setTitle("Сетевой журнал задач. Клиент.");
            setMinimumSize(new java.awt.Dimension(700, 385));
            setResizable(false);

            jScrollPane1.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    jScrollPane1MouseClicked(evt);
                }
            });

            taskTable.setColumnSelectionAllowed(true);
            taskTable.getTableHeader().setReorderingAllowed(false);
            Object[] column = {"Id", "Название", "Дата/время", "Завершённость"};
            DefaultTableModelNotEdit model = new DefaultTableModelNotEdit();
            model.setColumnIdentifiers(column);
            taskTable.setModel(model);
            taskTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
            taskTable.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mousePressed(java.awt.event.MouseEvent evt) {
                    taskTableMousePressed(evt);
                }
            });
            taskTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
            jScrollPane1.setViewportView(taskTable);

            javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
            jPanel1.setLayout(jPanel1Layout);
            jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 488, Short.MAX_VALUE)
            );
            jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 363, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE))
            );

            addButton.setText("Добавить задачу");
            addButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    addButtonActionPerformed(evt);
                }
            });

            editButton.setText("Изменить задачу");
            editButton.setEnabled(false);
            editButton.setMaximumSize(new java.awt.Dimension(121, 23));
            editButton.setMinimumSize(new java.awt.Dimension(121, 23));
            editButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    editButtonActionPerformed(evt);
                }
            });

            viewButton.setText("Просмотреть задачу");
            viewButton.setEnabled(false);
            viewButton.setMaximumSize(new java.awt.Dimension(121, 23));
            viewButton.setMinimumSize(new java.awt.Dimension(121, 23));
            viewButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    viewButtonActionPerformed(evt);
                }
            });

            deleteButton.setText("Удалить задачу");
            deleteButton.setEnabled(false);
            deleteButton.setMaximumSize(new java.awt.Dimension(121, 23));
            deleteButton.setMinimumSize(new java.awt.Dimension(121, 23));
            deleteButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    deleteButtonActionPerformed(evt);
                }
            });

            openCompleteTaskDialogButton.setText("Активные задачи");
            openCompleteTaskDialogButton.setEnabled(false);
            openCompleteTaskDialogButton.setMaximumSize(new java.awt.Dimension(121, 23));
            openCompleteTaskDialogButton.setMinimumSize(new java.awt.Dimension(121, 23));
            openCompleteTaskDialogButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    openCompleteTaskDialogButtonActionPerformed(evt);
                }
            });

            changeSoundAlertButton.setText("Изменение сигнала");
            changeSoundAlertButton.setMaximumSize(new java.awt.Dimension(121, 23));
            changeSoundAlertButton.setMinimumSize(new java.awt.Dimension(121, 23));
            changeSoundAlertButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    changeSoundAlertButtonActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
            jPanel2.setLayout(jPanel2Layout);
            jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                    .addContainerGap(20, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                        .addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(viewButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(editButton, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(deleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(changeSoundAlertButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(openCompleteTaskDialogButton, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(25, 25, 25))
            );
            jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(23, 23, 23)
                    .addComponent(addButton, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(viewButton, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(editButton, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(deleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(changeSoundAlertButton, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(openCompleteTaskDialogButton, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(16, 16, 16))
            );

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap())
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(0, 0, Short.MAX_VALUE)))
                    .addContainerGap())
            );

            pack();
        }// </editor-fold>//GEN-END:initComponents

    private void jScrollPane1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane1MouseClicked
        //Блокируем кнопки, если не выбрана задача
        viewButton.setEnabled(taskTable.getSelectedRow() != -1);
        editButton.setEnabled(taskTable.getSelectedRow() != -1);
        deleteButton.setEnabled(taskTable.getSelectedRow() != -1);
    }//GEN-LAST:event_jScrollPane1MouseClicked

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        //Подготавливаем окно к вводу
        taskDialog.setTitle("Добавить задачу");
        taskDialog.setVisible(true);
        nameTextField.setText("");
        descriptionTextArea.setText("");
        contactsTextArea.setText("");
        Calendar currentTime = Calendar.getInstance();
        currentTime.setTimeZone(TimeZone.getTimeZone("GMT+4"));
        dateChooser.setCalendar(currentTime);
        hourSpinField.setValue(currentTime.get(Calendar.HOUR_OF_DAY));
        minuteSpinField.setValue(currentTime.get(Calendar.MINUTE));
        secondSpinField.setValue(currentTime.get(Calendar.SECOND));
        normalPriorityRadioButton.setSelected(true);
        //И делаем их доступными для ввода
        nameTextField.setEditable(true);
        descriptionTextArea.setEditable(true);
        contactsTextArea.setEditable(true);
        dateChooser.setEnabled(true);
        hourSpinField.setEnabled(true);
        minuteSpinField.setEnabled(true);
        secondSpinField.setEnabled(true);
        normalPriorityRadioButton.setEnabled(true);
        highPriorityRadioButton.setEnabled(true);
        okButton.setEnabled(true);
        deleteButton.setEnabled(false);
        viewButton.setEnabled(false);
        editButton.setEnabled(false);
    }//GEN-LAST:event_addButtonActionPerformed

    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButtonActionPerformed
        Long idTask = (long) taskTable.getModel().getValueAt(
                taskTable.getSelectedRow(), 0);
        Task t = connection.getTask(idTask);
        if (t.isFinished()) {
            //Завершённые задачи нельзя изменять
            viewButtonActionPerformed(evt);
        } else {
            //Заполняем окно данными о задаче
            taskDialog.setTitle("Изменить задачу");
            taskDialog.setVisible(true);
            nameTextField.setText(t.getName());
            descriptionTextArea.setText(t.getDescription());
            contactsTextArea.setText(t.getContacts());
            dateChooser.setCalendar(t.getDate());
            hourSpinField.setValue(t.getDate().get(Calendar.HOUR_OF_DAY));
            minuteSpinField.setValue(t.getDate().get(Calendar.MINUTE));
            secondSpinField.setValue(t.getDate().get(Calendar.SECOND));
            if (t.isHighPriority()) {
                highPriorityRadioButton.setSelected(true);
            } else {
                normalPriorityRadioButton.setSelected(true);
            }
            //Разрешаем изменение данных
            nameTextField.setEditable(true);
            descriptionTextArea.setEditable(true);
            contactsTextArea.setEditable(true);
            dateChooser.setEnabled(true);
            hourSpinField.setEnabled(true);
            minuteSpinField.setEnabled(true);
            secondSpinField.setEnabled(true);
            normalPriorityRadioButton.setEnabled(true);
            highPriorityRadioButton.setEnabled(true);
            okButton.setEnabled(true);

        }
        deleteButton.setEnabled(false);
        viewButton.setEnabled(false);
        editButton.setEnabled(false);
    }//GEN-LAST:event_editButtonActionPerformed

    private void viewButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewButtonActionPerformed
        //Заполняем данные о задаче
        taskDialog.setTitle("Просмотреть задачу");
        taskDialog.setVisible(true);
        Long idTask = (long) taskTable.getModel().getValueAt(
                taskTable.getSelectedRow(), 0);
        Task t = connection.getTask(idTask);
        nameTextField.setText(t.getName());
        descriptionTextArea.setText(t.getDescription());
        contactsTextArea.setText(t.getContacts());
        dateChooser.setCalendar(t.getDate());
        hourSpinField.setValue(t.getDate().get(Calendar.HOUR_OF_DAY));
        minuteSpinField.setValue(t.getDate().get(Calendar.MINUTE));
        secondSpinField.setValue(t.getDate().get(Calendar.SECOND));
        if (t.isHighPriority()) {
            highPriorityRadioButton.setSelected(true);
        } else {
            normalPriorityRadioButton.setSelected(true);
        }
        //Запрещаем изменение данных
        nameTextField.setEditable(false);
        descriptionTextArea.setEditable(false);
        contactsTextArea.setEditable(false);
        dateChooser.setEnabled(false);
        hourSpinField.setEnabled(false);
        minuteSpinField.setEnabled(false);
        secondSpinField.setEnabled(false);
        normalPriorityRadioButton.setEnabled(false);
        highPriorityRadioButton.setEnabled(false);
        okButton.setEnabled(false);
        deleteButton.setEnabled(false);
        viewButton.setEnabled(false);
        editButton.setEnabled(false);
    }//GEN-LAST:event_viewButtonActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        if (JOptionPane.showConfirmDialog(this,
                "Вы уверены, что хотите удалить эту задачу?",
                "Подтвердите своё действие",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            long id = (long) taskTable.getValueAt(taskTable.getSelectedRow(), 0);
            connection.removeTask(id);
            //В таблице нет выделенной строки, блокируем кнопки
            deleteButton.setEnabled(false);
            viewButton.setEnabled(false);
            editButton.setEnabled(false);
        }
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        switch (taskDialog.getTitle()) {
            case "Добавить задачу": {
                if (!checkNameSpace(nameTextField.getText())) {
                    JOptionPane.showMessageDialog(taskDialog, "Неверный формат имени!");
                    return;
                }
                Calendar calendar = Calendar.getInstance();
                calendar.set(dateChooser.getCalendar().get(Calendar.YEAR),
                        dateChooser.getCalendar().get(Calendar.MONTH),
                        dateChooser.getCalendar().get(Calendar.DAY_OF_MONTH),
                        hourSpinField.getValue(), minuteSpinField.getValue(),
                        secondSpinField.getValue());
                if (!checkDataNewTask(calendar)) {
                    JOptionPane.showMessageDialog(taskDialog, "Нельзя вводить дату меньше текущей!");
                    return;
                }
                //Отправляем на сервер команду "Добавить задачу"
                connection.addTask(
                        nameTextField.getText(),
                        descriptionTextArea.getText(),
                        contactsTextArea.getText(),
                        calendar, false,
                        highPriorityRadioButton.isSelected());
                taskDialog.setVisible(false);
                break;
            }
            case "Изменить задачу": {
                if (!checkNameSpace(nameTextField.getText())) {
                    JOptionPane.showMessageDialog(taskDialog, "Неверный формат имени!");
                    return;
                }
                Calendar calendar = Calendar.getInstance();
                calendar.set(dateChooser.getCalendar().get(Calendar.YEAR),
                        dateChooser.getCalendar().get(Calendar.MONTH),
                        dateChooser.getCalendar().get(Calendar.DAY_OF_MONTH),
                        hourSpinField.getValue(), minuteSpinField.getValue(),
                        secondSpinField.getValue());
                if (!checkDataNewTask(calendar)) {
                    JOptionPane.showMessageDialog(taskDialog, "Нельзя вводить дату меньше текущей!");
                    return;
                }
                //Отправляем на сервер команду "Изменить задачу"
                long idTask = (long) taskTable.getValueAt(taskTable.getSelectedRow(), 0);
                Task t = connection.getTask(idTask);
                t.setDate(calendar);
                t.setName(nameTextField.getText());
                t.setContacts(contactsTextArea.getText());
                t.setDescription(descriptionTextArea.getText());
                t.setHighPriority(highPriorityRadioButton.isSelected());
                t.setWorkOnTask(false);
                //Отправляем на сервер команду "Изменить задачу"
                taskDialog.setVisible(false);
                connection.setTask(t);
                break;
            }
            default:
                break;
        }
    }//GEN-LAST:event_okButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        taskDialog.setVisible(false);
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void okParamButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okParamButtonActionPerformed
        //Сохраняем данные о порте и адресе и имени пользователя
        port = portTextField.getText();
        address = addressTextField.getText();
        nameUser = userTextField.getText();
        enterParamDialog.setVisible(false);
        portTextField.setText("");
        addressTextField.setText("");
        userTextField.setText("");
    }//GEN-LAST:event_okParamButtonActionPerformed

    private void enterParamDialogWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_enterParamDialogWindowClosing
        //dispose();
        System.exit(0);
    }//GEN-LAST:event_enterParamDialogWindowClosing

    private void prorogueCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prorogueCheckBoxActionPerformed
        //Разрешаем/запрещаем отложить задачу
        prorogueButton.setEnabled(prorogueCheckBox.isSelected());
        dateChooserRadioButton.setSelected(!prorogueCheckBox.isSelected());
        dateChooserRadioButtonActionPerformed(evt);
        dateChooserRadioButton.setEnabled(prorogueCheckBox.isSelected());
        dateComboBoxRadioButton.setEnabled(prorogueCheckBox.isSelected());
        jDateChooser1.setEnabled(prorogueCheckBox.isSelected());
        prorogueHourSpinField.setEnabled(prorogueCheckBox.isSelected());
        prorogueMinuteSpinField.setEnabled(prorogueCheckBox.isSelected());
        prorogueSecondSpinField.setEnabled(prorogueCheckBox.isSelected());
        //dateComboBox.setEnabled(dateComboBoxRadioButton.isSelected());
    }//GEN-LAST:event_prorogueCheckBoxActionPerformed

    private void dateChooserRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dateChooserRadioButtonActionPerformed
        //Выбираем новую дату оповещения вручную
        dateComboBox.setEnabled(false);
        jDateChooser1.setEnabled(true);
        jDateChooser1.setCalendar(Calendar.getInstance());
        prorogueHourSpinField.setEnabled(true);
        prorogueMinuteSpinField.setEnabled(true);
        prorogueSecondSpinField.setEnabled(true);
    }//GEN-LAST:event_dateChooserRadioButtonActionPerformed

    private void dateComboBoxRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dateComboBoxRadioButtonActionPerformed
        //Выбираем новую дату оповещения по заданному списку смещений
        dateComboBox.setEnabled(true);
        jDateChooser1.setEnabled(false);
        jDateChooser1.setCalendar(Calendar.getInstance());
        prorogueHourSpinField.setEnabled(false);
        prorogueMinuteSpinField.setEnabled(false);
        prorogueSecondSpinField.setEnabled(false);
    }//GEN-LAST:event_dateComboBoxRadioButtonActionPerformed

    private void prorogueButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prorogueButtonActionPerformed
        if (!checkNameSpace(completedTaskNameTextField.getText())) {
            JOptionPane.showMessageDialog(taskDialog, "Неверный формат имени!");
            return;
        }
        Task t = null;
        GregorianCalendar calendar = new GregorianCalendar();
        Calendar calendarDef = jDateChooser1.getCalendar();
        if (dateChooserRadioButton.isSelected()) {
            //отложить на определенную дату
            calendar.set(calendarDef.get(Calendar.YEAR),
                    calendarDef.get(Calendar.MONTH),
                    calendarDef.get(Calendar.DAY_OF_MONTH),
                    prorogueHourSpinField.getValue(),
                    prorogueMinuteSpinField.getValue(),
                    prorogueSecondSpinField.getValue());
            if (calendar.before(Calendar.getInstance())) {
                JOptionPane.showMessageDialog(completedTasksDialog,
                        "Нельзя вводить дату и время меньше текущей!");
                return;
            }
            if (completedTasksList.getSelectedIndex() == -1) {
                JOptionPane.showMessageDialog(completedTasksDialog,
                        "Выделите элемент списка");
                return;
            }
            t = (Task) completedTasksList.getSelectedValue();
            //Обновляем данные о задаче
            jDateChooser1.setCalendar(calendar);
            t.setName(completedTaskNameTextField.getText());
            t.setDescription(completedTaskDescriptionTextArea.
                    getText());
            t.setContacts(completedTaskContactsTextArea.getText());
            t.setDate(jDateChooser1.getCalendar());
            t.setWorkOnTask(false);
        } else {
            if (completedTasksList.getSelectedIndex() == -1) {
                JOptionPane.showMessageDialog(completedTasksDialog,
                        "Выделите элемент списка");
                return;
            }

            //Отложить на определённое время вперёд
            t = (Task) completedTasksList.getSelectedValue();
            switch (dateComboBox.getSelectedIndex()) {
                case 0: {
                    t.getDate().set(Calendar.HOUR_OF_DAY,
                            t.getDate().get(Calendar.HOUR_OF_DAY) + 1);
                    break;
                }
                case 1: {
                    t.getDate().set(Calendar.DAY_OF_MONTH,
                            t.getDate().get(Calendar.DAY_OF_MONTH) + 1);
                    break;
                }
                case 2: {
                    t.getDate().set(Calendar.DAY_OF_MONTH,
                            t.getDate().get(Calendar.DAY_OF_MONTH) + 7);
                    break;
                }
                case 3: {
                    t.getDate().set(Calendar.MONTH,
                            t.getDate().get(Calendar.MONTH) + 1);
                    break;
                }
            }
            t.setName(completedTaskNameTextField.getText());
            t.setDescription(completedTaskDescriptionTextArea.
                    getText());
            t.setContacts(completedTaskContactsTextArea.getText());
            t.setWorkOnTask(false);
        }
        //Удаляем
        ((DefaultListModel<Task>) completedTasksList.getModel()).
                removeElementAt(completedTasksList.getSelectedIndex());

        //Оповещаем сервер об изменении
        connection.setTask(t);
    }//GEN-LAST:event_prorogueButtonActionPerformed

    private void openCompleteTaskDialogButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openCompleteTaskDialogButtonActionPerformed
        completedTasksDialog.setVisible(true);

    }//GEN-LAST:event_openCompleteTaskDialogButtonActionPerformed

    private void finishButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_finishButtonActionPerformed
        if (completedTasksList.getSelectedIndex() == -1) {
            return;
        }
        //Завершаемая задача
        Task t = (Task) completedTasksList.getSelectedValue();
        t.setFinished(true);
        t.setWorkOnTask(false);
        t.setName(completedTaskNameTextField.getText());
        t.setDescription(completedTaskDescriptionTextArea.getText());
        t.setContacts(completedTaskContactsTextArea.getText());
        //Оповещаем сервер об изменении
        //connection.setAlertTask(t);

        ((DefaultListModel<Task>) completedTasksList.getModel()).
                removeElementAt(completedTasksList.getSelectedIndex());
        if (((DefaultListModel<Task>) completedTasksList.getModel()).getSize() == 0) {
            completedTasksDialog.setVisible(false);
            openCompleteTaskDialogButton.setEnabled(false);
        }

        connection.setTask(t);
    }//GEN-LAST:event_finishButtonActionPerformed

    private void deleteButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButton1ActionPerformed
        if (JOptionPane.showConfirmDialog(completedTasksDialog,
                "Вы уверены, что хотите удалить эту задачу?", null,
                JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE)
                == JOptionPane.YES_OPTION) {
            //Удаляемая задача
            Task t = (Task) completedTasksList.getSelectedValue();

            //Оповещаем сервер об изменении
            //connection.removeAlertTask(t.getId());
            connection.removeTask(t.getId());
            completedTasksList.remove(completedTasksList.getSelectedIndex());
            if (completedTasksList.getModel().getSize() == 0) {
                completedTasksDialog.setVisible(false);
                openCompleteTaskDialogButton.setEnabled(false);
            }
        }
    }//GEN-LAST:event_deleteButton1ActionPerformed

    private void changeSoundAlertButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeSoundAlertButtonActionPerformed
        deleteButton.setEnabled(false);
        viewButton.setEnabled(false);
        editButton.setEnabled(false);
        int ret = selectAlertFileChooser.showDialog(null, "Открыть файл");
        if (ret == JFileChooser.APPROVE_OPTION) {
            if (sound.testFile(selectAlertFileChooser.getSelectedFile())) {
                pathSound = selectAlertFileChooser.getSelectedFile().getAbsolutePath();
            } else {

                JOptionPane.showMessageDialog(this,
                        "Неверный формат файла");
            }
        }
    }//GEN-LAST:event_changeSoundAlertButtonActionPerformed

    private void portTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_portTextFieldKeyPressed
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            okParamButtonActionPerformed(null);
        }
    }//GEN-LAST:event_portTextFieldKeyPressed

    private void addressTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_addressTextFieldKeyPressed
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            okParamButtonActionPerformed(null);
        }
    }//GEN-LAST:event_addressTextFieldKeyPressed

    private void completedTasksDialogWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_completedTasksDialogWindowOpened
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+4"));
        jDateChooser1.setCalendar(calendar);
        prorogueHourSpinField.setValue(calendar.get(Calendar.HOUR_OF_DAY));
        prorogueMinuteSpinField.setValue(calendar.get(Calendar.MINUTE));
        prorogueSecondSpinField.setValue(calendar.get(Calendar.SECOND));
    }//GEN-LAST:event_completedTasksDialogWindowOpened

    private void taskTableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_taskTableMousePressed
        //Блокируем кнопки, если не выбрана задача
        viewButton.setEnabled(taskTable.getSelectedRow() != -1);
        editButton.setEnabled(taskTable.getSelectedRow() != -1);
        deleteButton.setEnabled(taskTable.getSelectedRow() != -1);
    }//GEN-LAST:event_taskTableMousePressed

    private void completedTasksListMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_completedTasksListMousePressed
        //Заполняем окно оповещения выбранной задачей
        if (evt.getButton() == MouseEvent.BUTTON1) {
            Task task = (Task) completedTasksList.getModel().
                    getElementAt(completedTasksList.getSelectedIndex());
            completedTaskNameTextField.setText(task.getName());
            completedTaskDescriptionTextArea.setText(task.getDescription());
            completedTaskContactsTextArea.setText(task.getContacts());
        }
    }//GEN-LAST:event_completedTasksListMousePressed

    //Заполнение таблицы задач
    public void fillTable(final Collection<Task> tasks) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Object[] column = {"Id", "Название", "Дата/время", "Завершённость"};
                DefaultTableModelNotEdit model = new DefaultTableModelNotEdit();
                model.setColumnIdentifiers(column);
                model.setRowCount(0);
                for (Task task : tasks) {
                    if (task.isFinished()) {
                        model.addRow(new Object[]{task.getId(), task.getName(),
                            task.getDate().getTime(), "Завершена"});
                    } else {
                        model.addRow(new Object[]{task.getId(), task.getName(),
                            task.getDate().getTime(), "Не завершена"});
                    }
                }
                taskTable.setModel(model);
                for (int i = 0; i < taskTable.getColumnCount(); i++) {
                    taskTable.getColumnModel().getColumn(i).setCellRenderer(renderer);
                }
            }
        });
    }

    //Заполнение списка задач на оповещение по индексам в списке всех задач
    public void alert(final List<Long> alertTasksId, final boolean soundFlag, final boolean visibleDialog) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                //Пришёл пустой
                if (alertTasksId.isEmpty()) {
                    ((DefaultListModel<Task>) completedTasksList.getModel()).clear();
                    completedTasksDialog.setVisible(false);
                    openCompleteTaskDialogButton.setEnabled(false);
                    return;
                }
                if (soundFlag) {
                    //устанавливаем звуковой файл
                    sound.playFile(new File(pathSound));
                    //запускаем звуковой файл в потоке
                    sound.start();
                }

                ((DefaultListModel<Task>) completedTasksList.getModel()).clear();
                for (long id : alertTasksId) {
                    for (Task t : connection.getAllUserTasks()) {
                        if (t.getId() == id) {
                            ((DefaultListModel<Task>) completedTasksList.getModel())
                                    .addElement(t);
                        }
                    }
                }
                //И заполняем данными окно при необходимости
                if (completedTasksList.getSelectedIndex() == -1) {
                    completedTasksList.setSelectedIndex(0);
                    Task t = (Task) completedTasksList.getSelectedValue();
                    completedTaskNameTextField.setText(t.getName());
                    completedTaskContactsTextArea.setText(t.getContacts());
                    completedTaskDescriptionTextArea.setText(t.getDescription());
                    openCompleteTaskDialogButton.setEnabled(true);
                }
                openCompleteTaskDialogButton.setEnabled(true);
                completedTasksDialog.setVisible(visibleDialog);
            }
        });
    }

    public void disconnect() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                //Потеряно соединение, требуем ввода данных о сервере
                connection = null;
                port = null;
                address = null;
                nameUser = null;
                completedTasksDialog.setVisible(false);
                taskDialog.setVisible(false);
                GUI.this.setVisible(false);

                boolean flag = true;
                while (flag) {
                    try {
                        enterParamDialog.setLocationRelativeTo(null);
                        enterParamDialog.setModal(false);
                        enterParamDialog.setVisible(true);

                        if (nameUser == null || address == null || port == null) {
                            System.exit(0);
                        }

                        try {

                            //И пытаемся подключиться
                            connection = new ConnectionClass(
                                    address, Integer.parseInt(port), nameUser, GUI.this);
                            flag = false;
                        } catch (NumberFormatException e) {
                            JOptionPane.showMessageDialog(GUI.this,
                                    "Введены некорректные данные.", "Ошибка",
                                    JOptionPane.ERROR_MESSAGE);
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(GUI.this,
                                    "Не удалось подключиться к удалённому серверу",
                                    "Ошибка", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (StringIndexOutOfBoundsException ex) {
                        JOptionPane.showMessageDialog(GUI.this,
                                "Введены некорректные данные.", "Ошибка",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }

                //System.exit(0);
                connection.getAllTasks(nameUser);
                GUI.this.setVisible(true);
            }
        });

    }

    private boolean checkDataNewTask(Calendar calendar) {
        return calendar.after(GregorianCalendar.getInstance());

    }

    private boolean checkNameSpace(String value) {
        if (value.equals("")) {
            return false;
        }
        char[] array = value.toCharArray();
        for (int i = 0; i < array.length; i++) {
            if (array[i] != ' ') {
                return true;
            }
        }
        return false;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JLabel addressLabel;
    private javax.swing.JTextField addressTextField;
    private javax.swing.JPanel buttonsPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton changeSoundAlertButton;
    private javax.swing.JTextArea completedTaskContactsTextArea;
    private javax.swing.JTextArea completedTaskDescriptionTextArea;
    private javax.swing.JTextField completedTaskNameTextField;
    private javax.swing.JDialog completedTasksDialog;
    private javax.swing.JPanel completedTasksInfoPanel;
    private javax.swing.JList completedTasksList;
    private javax.swing.JPanel completedTasksListPanel;
    private javax.swing.JPanel contactsPanel;
    private javax.swing.JTextArea contactsTextArea;
    private com.toedter.calendar.JDateChooser dateChooser;
    private javax.swing.JRadioButton dateChooserRadioButton;
    private javax.swing.JComboBox dateComboBox;
    private javax.swing.JRadioButton dateComboBoxRadioButton;
    private javax.swing.JPanel dateTimePanel;
    private javax.swing.ButtonGroup datebuttonGroup;
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton deleteButton1;
    private javax.swing.JPanel descriptionPanel;
    private javax.swing.JTextArea descriptionTextArea;
    private javax.swing.JButton editButton;
    private javax.swing.JDialog enterParamDialog;
    private javax.swing.JButton finishButton;
    private javax.swing.JRadioButton highPriorityRadioButton;
    private com.toedter.components.JSpinField hourSpinField;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private com.toedter.components.JSpinField minuteSpinField;
    private javax.swing.JPanel namePanel;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JRadioButton normalPriorityRadioButton;
    private javax.swing.JButton okButton;
    private javax.swing.JButton okParamButton;
    private javax.swing.JButton openCompleteTaskDialogButton;
    private javax.swing.JLabel portLabel;
    private javax.swing.JTextField portTextField;
    private javax.swing.ButtonGroup priorityButtonGroup;
    private javax.swing.JPanel priorityPanel;
    private javax.swing.JButton prorogueButton;
    private javax.swing.JCheckBox prorogueCheckBox;
    private com.toedter.components.JSpinField prorogueHourSpinField;
    private com.toedter.components.JSpinField prorogueMinuteSpinField;
    private com.toedter.components.JSpinField prorogueSecondSpinField;
    private com.toedter.components.JSpinField secondSpinField;
    private javax.swing.JFileChooser selectAlertFileChooser;
    private javax.swing.JFrame taskDialog;
    private javax.swing.JTable taskTable;
    private javax.swing.JLabel userLabel;
    private javax.swing.JTextField userTextField;
    private javax.swing.JButton viewButton;
    // End of variables declaration//GEN-END:variables
}