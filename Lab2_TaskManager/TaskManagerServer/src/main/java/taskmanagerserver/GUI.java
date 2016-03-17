package taskmanagerserver;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import task.Task;
import taskmanagerserver.core.*;
import user.User;

public class GUI extends javax.swing.JFrame {

    /* Поток, обрабатывающий подключения к серверу */
    private ServerSocket serverSocket;
    /* Динамический массив подключений; Для каждого соединения с клиентом
       свой объект ConnectionClass*/
    private List<ConnectionClass> connections;
    /* Объект для работы со списком задач */
    private TaskManager manager;
    /* Система оповещения */
    private AlertSystem alertSystem;
    /* Рендерер для таблицы, окрашивает в красный высокоприоритетные задачи */
    private DefaultTableCellRenderer renderer;

    public GUI() {
        initComponents();
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+4"));
        //Создаём серверный поток
        Thread serverSocketThread = new Thread() {
            @Override
            public void run() {
                //Пытаемся поднять сервер
                boolean flag = true;
                while (flag) {
                    try {
                        int port = Integer.parseInt(JOptionPane.
                                showInputDialog("Введите номер порта"));
                        serverSocket = new ServerSocket(port);
                        flag = false;
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(GUI.this,
                                "Ошибка при старте сервера.", "Ошибка",
                                JOptionPane.ERROR_MESSAGE);
                        System.exit(-1);
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(GUI.this,
                                "Введены некорректные данные.", "Ошибка",
                                JOptionPane.ERROR_MESSAGE);
                        System.exit(-1);
                    }
                }
                GUI.this.setVisible(true);
                connections = new ArrayList<>();

                //Ожидаем подключения
                while (true) {
                    try {
                        ConnectionClass connection = new ConnectionClass(
                                serverSocket.accept(), GUI.this, 
                                ConcreteTaskManager.getInstance());
                        connections.add(connection);
                        alertSystem.addListener(connection);
                    } catch (IOException ioe) {
                        JOptionPane.showMessageDialog(GUI.this,
                                "Не удалось установить соединение с клиентом.",
                                "Ошибка", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        };
        manager = ConcreteTaskManager.getInstance();
        ArrayList<User> list = new ArrayList<>();
        list.add(new User("All users"));
        list.addAll(manager.getCollectionUser());
        DefaultComboBoxModel<User> comboBoxModel = 
                new DefaultComboBoxModel(list.toArray());
        usersComboBox.setModel(comboBoxModel);
        usersComboBox.setSelectedIndex(0);
        /*Инициализация и запуск системы оповещения*/
        alertSystem = ConcreteAlertSystem.getInstance();
        Thread alertThread = new Thread((Runnable) alertSystem);
        alertThread.setDaemon(true);
        serverSocketThread.setDaemon(true);
        alertThread.start();
        serverSocketThread.start();
        //Заполняем таблицу задачами
        User nameUser = (User) usersComboBox.getSelectedItem();
        fillTableSelectedUser(nameUser.getName());
        taskTable.setAutoCreateRowSorter(true);
        //Ставим по центру
        this.setLocationRelativeTo(null);
    }
 
    //Вызывается при изменении задач
    
    public void update(){
        EventQueue.invokeLater(new Runnable(){
           @Override
           public void run(){
               User user = (User) usersComboBox.getSelectedItem();
               //String nameUser = (String) usersComboBox.getSelectedItem();
               fillTableSelectedUser(user.getName());
               notifyConnections();
           }
        });        
    }

    public void addUserToComboBox(final User user){
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                //usersComboBox.addItem(user);
                ((DefaultComboBoxModel<User>)usersComboBox.getModel()).addElement(user);
            }
        });
    }
    
    //заполнение таблицы задачами в зависимости от выбранного значения userComboBox
    public void fillTableSelectedUser(String name) {
        Object[] column = {"Id", "Название", "Дата/время", "Завершённость"};
        DefaultTableModelNotEdit model = new DefaultTableModelNotEdit();
        model.setColumnIdentifiers(column);
        //Task task;
        synchronized (manager) {
            User user;
            Collection<Task> list;
            if(name.equals("All users")){
                list = manager.getCollectionTask();
            }
            else{
                user = manager.searchUser(name);
                list = manager.getCollectionTasksForUser(user.getId());
            }
                        
            for (Task task : list) {
                if (task.isFinished()) {
                    model.addRow(new Object[]{task.getId(), task.getName(),
                        task.getDate().getTime(), "Завершена"});
                } else {
                    model.addRow(new Object[]{task.getId(), task.getName(),
                        task.getDate().getTime(), "Не завершена"});
                }
            }
        }
        taskTable.setModel(model);
        /* Выделение приоритета красным, добавление сортировки в таблицу */
        renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table,
                    Object value, boolean isSelected, boolean hasFocus,
                    int row, int column) {
                Component cell = super.getTableCellRendererComponent(table,
                        value, hasFocus, hasFocus, row, column);
                long idTask = (long) table.getValueAt(row, 0);
                Task cellValue = manager.getTask(idTask);
                if (cellValue.isHighPriority()) {
                    cell.setForeground(Color.RED);
                } else {
                    cell.setForeground(Color.BLACK);
                }
                return cell;
            }
        };
        for (int i = 0; i < taskTable.getColumnCount(); i++) {
            taskTable.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
        if (taskTable.getRowCount() == 0) {
            editButton.setEnabled(false);
            deleteButton.setEnabled(false);
            viewButton.setEnabled(false);
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
        selectUserPanel = new javax.swing.JPanel();
        selectUserLabel = new javax.swing.JLabel();
        selectUserComboBox = new javax.swing.JComboBox();
        buttonsPanel = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        priorityButtonGroup = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        taskTable = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        addButton = new javax.swing.JButton();
        editButton = new javax.swing.JButton();
        viewButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        usersComboBox = new javax.swing.JComboBox();

        taskDialog.setLocationByPlatform(true);
        taskDialog.setMinimumSize(new java.awt.Dimension(410, 480));
        taskDialog.setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
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

        selectUserLabel.setText("Пользователь:");

        javax.swing.GroupLayout selectUserPanelLayout = new javax.swing.GroupLayout(selectUserPanel);
        selectUserPanel.setLayout(selectUserPanelLayout);
        selectUserPanelLayout.setHorizontalGroup(
            selectUserPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(selectUserPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(selectUserLabel)
                .addGap(18, 18, 18)
                .addComponent(selectUserComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        selectUserPanelLayout.setVerticalGroup(
            selectUserPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(selectUserPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(selectUserPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(selectUserLabel)
                    .addComponent(selectUserComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
            .addGroup(taskDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(taskDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(priorityPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dateTimePanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(contactsPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(descriptionPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(namePanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(selectUserPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(buttonsPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addComponent(selectUserPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(buttonsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Сетевой журнал задач. Сервер.");
        setMinimumSize(new java.awt.Dimension(690, 330));
        setName("mainFrame"); // NOI18N
        setPreferredSize(new java.awt.Dimension(690, 280));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        Object[] column = {"Id", "Название", "Дата/время", "Завершённость"};
        DefaultTableModelNotEdit model = new DefaultTableModelNotEdit();
        model.setColumnIdentifiers(column);
        taskTable.setModel(model);
        taskTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        taskTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_NEXT_COLUMN);
        taskTable.setSelectionBackground(new java.awt.Color(0, 204, 204));
        taskTable.getTableHeader().setReorderingAllowed(false);
        taskTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                taskTableMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(taskTable);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 488, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE)
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
        editButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editButtonActionPerformed(evt);
            }
        });

        viewButton.setText("Просмотреть задачу");
        viewButton.setEnabled(false);
        viewButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewButtonActionPerformed(evt);
            }
        });

        deleteButton.setText("Удалить задачу");
        deleteButton.setEnabled(false);
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        usersComboBox.setMaximumRowCount(50);
        usersComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "User 1", "User 2", "User 3", "User 4" }));
        usersComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usersComboBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(editButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(viewButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(deleteButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(addButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(usersComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 25, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(viewButton, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(editButton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(deleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(usersComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(130, Short.MAX_VALUE))
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

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        //Подготавливаем окно для ввода данных
        taskDialog.setTitle("Добавить задачу");        
        nameTextField.setText("");
        descriptionTextArea.setText("");
        contactsTextArea.setText("");
        Calendar currentTime = Calendar.getInstance();
        dateChooser.setCalendar(currentTime);
        hourSpinField.setValue(currentTime.get(Calendar.HOUR_OF_DAY));
        minuteSpinField.setValue(currentTime.get(Calendar.MINUTE));
        secondSpinField.setValue(currentTime.get(Calendar.SECOND));
        normalPriorityRadioButton.setSelected(true);
        Collection<User> users;
        synchronized(manager){
            users = manager.getCollectionUser();
        }
        if(users.isEmpty()){
            JOptionPane.showMessageDialog(taskDialog, "Нет подключенных пользователей");
            return;
        }
        DefaultComboBoxModel<User> comboBoxModel = 
                new DefaultComboBoxModel(users.toArray());
        selectUserComboBox.setModel(comboBoxModel);
        selectUserComboBox.setSelectedIndex(0);
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
        editButton.setEnabled(false);
        viewButton.setEnabled(false);
        taskDialog.setVisible(true);
    }//GEN-LAST:event_addButtonActionPerformed

    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButtonActionPerformed
        Long idTask = (long) taskTable.getModel().getValueAt(
                taskTable.getSelectedRow(), 0);
        Task t = null;
        synchronized(manager){
            t = manager.getTask(idTask);
        }
        if (t.isFinished()) {
            //Завершённые задачи можно только просматривать
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
            
            DefaultComboBoxModel<User> comboBoxModel
                    = new DefaultComboBoxModel(new User[]{t.getUser()});
            selectUserComboBox.setModel(comboBoxModel);
            selectUserComboBox.setSelectedIndex(0);
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
        taskDialog.setTitle("Просмотреть задачу");
        taskDialog.setVisible(true);
        Long idTask = (long) taskTable.getModel().getValueAt(
                taskTable.getSelectedRow(), 0);
        Task t = null;
        synchronized(manager){
            t = manager.getTask(idTask);
        }
        //Заполняем окно данными о команде
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
        DefaultComboBoxModel<User> comboBoxModel
                = new DefaultComboBoxModel(new User[]{t.getUser()});
        selectUserComboBox.setModel(comboBoxModel);
        selectUserComboBox.setSelectedIndex(0);
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
            long idTask = (long) taskTable.getModel().getValueAt(
                    taskTable.getSelectedRow(), 0);

            synchronized (manager) {
                manager.removeTask(idTask);
            }
            this.update();
            viewButton.setEnabled(false);
            editButton.setEnabled(false);
            deleteButton.setEnabled(false);
        }
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        switch (taskDialog.getTitle()) {
            case "Добавить задачу": {
                //чтобы добавить определенному пользователю задачу
                //его нужно выбрать в главном окне
                if(!checkNameSpace(nameTextField.getText())){
                    JOptionPane.showMessageDialog(taskDialog, "Неверный формат имени!");
                    return;
                }
                Calendar calendar = Calendar.getInstance();
                calendar.set(dateChooser.getCalendar().get(Calendar.YEAR),
                        dateChooser.getCalendar().get(Calendar.MONTH),
                        dateChooser.getCalendar().get(Calendar.DAY_OF_MONTH),
                        hourSpinField.getValue(), minuteSpinField.getValue(),
                        secondSpinField.getValue());
                if(!checkDataNewTask(calendar)){
                    JOptionPane.showMessageDialog(taskDialog, "Нельзя вводить дату меньше текущей!");
                    return;
                }
                synchronized (manager) {
                    User user = (User)selectUserComboBox.getSelectedItem();
                    manager.addTask(new Task(nameTextField.getText(),
                            descriptionTextArea.getText(),
                            contactsTextArea.getText(),
                            calendar, false,
                            highPriorityRadioButton.isSelected(),
                            null,user
                    ));
                }
                User user = (User)usersComboBox.getSelectedItem();
                //String nameUser = (String) usersComboBox.getSelectedItem();
                fillTableSelectedUser(user.getName());
                taskDialog.setVisible(false);
                notifyConnections();
            }
            break;
            case "Изменить задачу": {
                if(!checkNameSpace(nameTextField.getText())){
                    JOptionPane.showMessageDialog(taskDialog, "Неверный формат имени!");
                    return;
                }
                synchronized (manager) {
                    long id = (long) taskTable.getModel().getValueAt(
                            taskTable.getSelectedRow(), 0);
                    Task t = manager.getTask(id);
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
                    t.setName(nameTextField.getText());
                    t.setDescription(descriptionTextArea.getText());
                    t.setContacts(contactsTextArea.getText());
                    t.setDate(calendar);
                    t.setHighPriority(highPriorityRadioButton.isSelected());
                    if (t.getWorkOnTask()) {
                        t.setWorkOnTask(false);
                    }
                    
                    User user = (User) usersComboBox.getSelectedItem();
                    //String nameUser = (String) usersComboBox.getSelectedItem();
                    fillTableSelectedUser(user.getName());
                    taskDialog.setVisible(false);
                    notifyConnections();
                }

            }
            break;
            default:
                //Просмотреть задачу
                taskDialog.setVisible(false);
                break;
        }
    }//GEN-LAST:event_okButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        taskDialog.setVisible(false);
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        try {
            manager.saveTasks();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Не удалось сохранить журнал заданий");
        }
        this.dispose();
    }//GEN-LAST:event_formWindowClosing

    private void taskTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_taskTableMouseClicked
        //Блокируем кнопки, если не выбрана задача
        viewButton.setEnabled(taskTable.getSelectedRow() != -1);
        editButton.setEnabled(taskTable.getSelectedRow() != -1);
        deleteButton.setEnabled(taskTable.getSelectedRow() != -1);
    }//GEN-LAST:event_taskTableMouseClicked

    private void usersComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usersComboBoxActionPerformed
        User user = (User) usersComboBox.getSelectedItem();
        //String nameUser = (String) usersComboBox.getSelectedItem();
        fillTableSelectedUser(user.getName());
    }//GEN-LAST:event_usersComboBoxActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GUI().setVisible(false);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JPanel buttonsPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JPanel contactsPanel;
    private javax.swing.JTextArea contactsTextArea;
    private com.toedter.calendar.JDateChooser dateChooser;
    private javax.swing.JPanel dateTimePanel;
    private javax.swing.JButton deleteButton;
    private javax.swing.JPanel descriptionPanel;
    private javax.swing.JTextArea descriptionTextArea;
    private javax.swing.JButton editButton;
    private javax.swing.JRadioButton highPriorityRadioButton;
    private com.toedter.components.JSpinField hourSpinField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private com.toedter.components.JSpinField minuteSpinField;
    private javax.swing.JPanel namePanel;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JRadioButton normalPriorityRadioButton;
    private javax.swing.JButton okButton;
    private javax.swing.ButtonGroup priorityButtonGroup;
    private javax.swing.JPanel priorityPanel;
    private com.toedter.components.JSpinField secondSpinField;
    private javax.swing.JComboBox selectUserComboBox;
    private javax.swing.JLabel selectUserLabel;
    private javax.swing.JPanel selectUserPanel;
    private javax.swing.JFrame taskDialog;
    private javax.swing.JTable taskTable;
    private javax.swing.JComboBox usersComboBox;
    private javax.swing.JButton viewButton;
    // End of variables declaration//GEN-END:variables

    private void notifyConnections() {
        for (ConnectionClass c : connections) {
            c.sendTaskAndAlertTask();
        }
    }

    public void disconnect(ConnectionClass aThis) {
        connections.remove(aThis);
    }
    
    private boolean checkDataNewTask(Calendar calendar){
        return calendar.after(GregorianCalendar.getInstance());
        
    }
    
    private boolean checkNameSpace(String value){
        if(value.equals("")) return false;
        char[] array=value.toCharArray();
        for(int i=0; i< array.length; i++){
            if(array[i]!= ' '){
                return true;
            }
        }
        return false;
    }
}
