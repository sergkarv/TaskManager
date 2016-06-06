 /*  
 * Classname: gui
 * Autors: Карасев С.В.
 * March 2016
 * 
 * Task Manager
 */
package taskmanager;

//import com.toedter.calendar.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.*;
import taskmanager.core.*;

/**
 * Графический интерфейс программы
 * @author Карасев
 * @version 1.0
 */
public class gui extends javax.swing.JFrame implements Listener {

    /** Объект для управления журналом */
    private TaskManager manager;
    /** Система оповещения о задании */
    //private AlertSystem alertSystem;
    /** Модель для таблицы выполненных задач */
    private DefaultListModel<Task> completedListModel;
    /** Системный трей */
    private SystemTray sysTray = SystemTray.getSystemTray();
    /** Иконка в трее */
    private TrayIcon trayIcon;
    /** Объект для разкрашивания таблицы по приоритету */
    private DefaultTableCellRenderer renderer;
    /** Всплывающее меню для таблицы задач */
    private JPopupMenu popup = new JPopupMenu();
    /** Объект для работы со звуком */
    private Sound sound = new Sound();
    /** Флаг, поясняющий, обновляем мы задачу или создаем новую */
    private boolean flagAddOrUpdate = false;
    
    private final String STANDART_ICON = "images/standartIcon.gif";
    private final String COMPLETED_ICON = "images/completedTasksIcon.gif";
    private final String STANDART_SIGNAL = "signal/bell.wav";
    
    /**
     * Создает новую форму
     * @param manager
     */
    public gui(TaskManager tman) {
        initComponents();        
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+4"));
        //Изменение размеров таблицы
        tablePanel.setPreferredSize(
                new Dimension(this.getWidth() * 2 / 3, this.getHeight()));
        //Настройка ширины колонок
        JTableHeader th = taskTable.getTableHeader();
        TableColumn column = taskTable.getColumnModel().getColumn(0);
        column.setPreferredWidth(5);
        column = taskTable.getColumnModel().getColumn(1);
        column.setPreferredWidth(10);
        column = taskTable.getColumnModel().getColumn(2);
        int prefWidth =
                Math.round(
                (float) th.getFontMetrics(th.getFont()).
                getStringBounds(th.getTable().getColumnName(2),
                th.getGraphics()).getWidth());
        column.setPreferredWidth(prefWidth + 10);
        column = taskTable.getColumnModel().getColumn(3);
        column.setPreferredWidth(tablePanel.getWidth() - prefWidth + 55);
        this.manager = tman;
        /*Вывод на экран данных по фильтрам*/
        changingTable(activityComboBox.getSelectedIndex(),
                priorityComboBox.getSelectedIndex());
        /*Подготовка трея*/
        try {
            trayIcon = new TrayIcon(ImageIO.read(new File(STANDART_ICON)),
                    "Нет готовых заданий");
        } catch (IOException e) {
        }
        trayIcon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(true);
                setState(JFrame.NORMAL);
                removeTrayIcon();
                try {
                    if (completedTasksList.getModel().getSize() != 0
                            && !completedTasksDialog.isVisible()) {
                        trayIcon.setImage(ImageIO.read(
                                new File(STANDART_ICON)));
                        trayIcon.setToolTip(completedListModel.getSize()
                                + " готовых(ое) задания(е)");
                        completedTasksDialog.setVisible(true);
                    }
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });
        /* Всплывающее меню для иконки в трее для закрытия программы */
        PopupMenu popupMenu = new PopupMenu();
        MenuItem item = new MenuItem("Exit");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                System.exit(0);
            }
        });
        popupMenu.add(item);
        trayIcon.setPopupMenu(popupMenu);
        /* Сворачивание в трей */
        addWindowStateListener(new WindowStateListener() {
            @Override
            public void windowStateChanged(WindowEvent e) {
                if (e.getNewState() == JFrame.ICONIFIED) {
                    setVisible(false);
                    addTrayIcon();
                }
            }
        });
        /* Выделение приоритета красным, добавление сортировки в таблицу */
        renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table,
                    Object value, boolean isSelected, boolean hasFocus,
                    int row, int column) {
                Component cell = super.getTableCellRendererComponent(table,
                        value, hasFocus, hasFocus, row, column);
                long idTask = (long)table.getValueAt(row, 0);
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
        taskTable.setAutoCreateRowSorter(true);
        /* Добавление всплывающего меню на таблицу с задачами */
        JMenuItem editItem = new JMenuItem("Изменить/просмотреть");
        editItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                editButtonActionPerformed(ae);
            }
        });
        JMenuItem removeItem = new JMenuItem("Удалить");
        removeItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                removeButtonActionPerformed(ae);
            }
        });
        popup.add(editItem);
        popup.add(removeItem);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        newTaskDialog = new javax.swing.JDialog();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        titleTextField = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        descriptionTextArea = new javax.swing.JTextArea();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        contactsTextArea = new javax.swing.JTextArea();
        jDateChooser = new com.toedter.calendar.JDateChooser();
        hourSpinField = new com.toedter.components.JSpinField();
        minuteSpinField = new com.toedter.components.JSpinField();
        secondSpinField = new com.toedter.components.JSpinField();
        jLabel11 = new javax.swing.JLabel();
        soundTextField = new javax.swing.JTextField();
        soundChooseButton = new javax.swing.JButton();
        standartSoundCheckBox = new javax.swing.JCheckBox();
        priorityLabel = new javax.swing.JLabel();
        normalRadioButton = new javax.swing.JRadioButton();
        highRadioButton = new javax.swing.JRadioButton();
        completedTasksDialog = new javax.swing.JDialog();
        completedTasksListPanel = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        completedTasksList = new javax.swing.JList();
        completedTasksInfoPanel = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        completedTaskNameTextField = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        completedTaskDescriptionTextArea = new javax.swing.JTextArea();
        jScrollPane5 = new javax.swing.JScrollPane();
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
        deleteButton = new javax.swing.JButton();
        finishButton = new javax.swing.JButton();
        selectAlertFileChooser = new javax.swing.JFileChooser();
        datebuttonGroup = new javax.swing.ButtonGroup();
        prioritybuttonGroup = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        tablePanel = new javax.swing.JPanel();
        tableScrollPane = new javax.swing.JScrollPane();
        taskTable = new javax.swing.JTable();
        controlPanel = new javax.swing.JPanel();
        addButton = new javax.swing.JButton();
        removeButton = new javax.swing.JButton();
        editButton = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        priorityComboBox = new javax.swing.JComboBox();
        activityComboBox = new javax.swing.JComboBox();
        showCompletedDialogButton = new javax.swing.JButton();

        newTaskDialog.setMinimumSize(new java.awt.Dimension(430, 470));
        newTaskDialog.setModal(true);
        newTaskDialog.setResizable(false);

        jLabel2.setText("Название:");

        jLabel3.setText("Описание:");

        jLabel4.setText("Дата/время:");

        jLabel5.setText("Контакты:");

        descriptionTextArea.setColumns(20);
        descriptionTextArea.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        descriptionTextArea.setLineWrap(true);
        descriptionTextArea.setRows(5);
        descriptionTextArea.setWrapStyleWord(true);
        descriptionTextArea.setPreferredSize(jScrollPane1.getPreferredSize());
        jScrollPane1.setViewportView(descriptionTextArea);

        okButton.setText("ОК");
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

        contactsTextArea.setColumns(20);
        contactsTextArea.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        contactsTextArea.setLineWrap(true);
        contactsTextArea.setRows(5);
        contactsTextArea.setWrapStyleWord(true);
        contactsTextArea.setPreferredSize(jScrollPane1.getPreferredSize());
        jScrollPane3.setViewportView(contactsTextArea);

        hourSpinField.setMaximum(23);
        hourSpinField.setMinimum(0);

        minuteSpinField.setMaximum(59);
        minuteSpinField.setMinimum(0);

        secondSpinField.setMaximum(59);
        secondSpinField.setMinimum(0);

        jLabel11.setText("Звуковое оповещение:");

        soundTextField.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        soundTextField.setEnabled(false);

        soundChooseButton.setText("Обзор");
        soundChooseButton.setEnabled(false);
        soundChooseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                soundChooseButtonActionPerformed(evt);
            }
        });

        standartSoundCheckBox.setSelected(true);
        standartSoundCheckBox.setText("Использовать стандартный звуковой файл");
        standartSoundCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                standartSoundCheckBoxActionPerformed(evt);
            }
        });

        priorityLabel.setText("Приоритет:");

        prioritybuttonGroup.add(normalRadioButton);
        normalRadioButton.setSelected(true);
        normalRadioButton.setText("Обычный");

        prioritybuttonGroup.add(highRadioButton);
        highRadioButton.setText("Высокий");

        javax.swing.GroupLayout newTaskDialogLayout = new javax.swing.GroupLayout(newTaskDialog.getContentPane());
        newTaskDialog.getContentPane().setLayout(newTaskDialogLayout);
        newTaskDialogLayout.setHorizontalGroup(
            newTaskDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(newTaskDialogLayout.createSequentialGroup()
                .addGroup(newTaskDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(newTaskDialogLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(standartSoundCheckBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(newTaskDialogLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(newTaskDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(newTaskDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(titleTextField)
                            .addComponent(jScrollPane1)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(newTaskDialogLayout.createSequentialGroup()
                                .addComponent(jDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(hourSpinField, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(minuteSpinField, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(secondSpinField, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(newTaskDialogLayout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(41, 41, 41))
                    .addGroup(newTaskDialogLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(newTaskDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(newTaskDialogLayout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(soundTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(soundChooseButton))
                            .addComponent(priorityLabel)
                            .addGroup(newTaskDialogLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(newTaskDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(normalRadioButton)
                                    .addComponent(highRadioButton))))
                        .addGap(0, 25, Short.MAX_VALUE)))
                .addContainerGap())
        );
        newTaskDialogLayout.setVerticalGroup(
            newTaskDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(newTaskDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(newTaskDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(titleTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(newTaskDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(newTaskDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(newTaskDialogLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4))
                    .addGroup(newTaskDialogLayout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(newTaskDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(hourSpinField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(minuteSpinField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(secondSpinField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(newTaskDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(soundTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(soundChooseButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(standartSoundCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(priorityLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(normalRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(highRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 52, Short.MAX_VALUE)
                .addGroup(newTaskDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jDateChooser.setCalendar(new GregorianCalendar());

        completedTasksDialog.setMinimumSize(new java.awt.Dimension(663, 396));
        completedTasksDialog.setResizable(false);

        completedTasksListPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Список заданий");

        completedTasksList.setModel(new DefaultListModel<Task>());
        completedTasksList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        completedTasksList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                completedTasksListMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(completedTasksList);

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
                .addComponent(jScrollPane2)
                .addContainerGap())
        );
        completedTasksListPanelLayout.setVerticalGroup(
            completedTasksListPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(completedTasksListPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel7.setText("Название:");

        jLabel12.setText("Описание:");

        jLabel13.setText("Контакты:");

        completedTaskDescriptionTextArea.setColumns(20);
        completedTaskDescriptionTextArea.setRows(5);
        jScrollPane4.setViewportView(completedTaskDescriptionTextArea);

        completedTaskContactsTextArea.setColumns(20);
        completedTaskContactsTextArea.setRows(5);
        jScrollPane5.setViewportView(completedTaskContactsTextArea);

        jDateChooser1.setEnabled(false);
        jDateChooser1.setCalendar(new GregorianCalendar());

        prorogueHourSpinField.setEnabled(false);

        prorogueMinuteSpinField.setEnabled(false);

        prorogueSecondSpinField.setEnabled(false);

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

        deleteButton.setText("Удалить");
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
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
                        .addGap(5, 5, 5)
                        .addGroup(completedTasksInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(completedTasksInfoPanelLayout.createSequentialGroup()
                                .addComponent(dateComboBoxRadioButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(dateComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(completedTasksInfoPanelLayout.createSequentialGroup()
                                .addComponent(dateChooserRadioButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(prorogueHourSpinField, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(prorogueMinuteSpinField, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                        .addComponent(prorogueSecondSpinField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(completedTasksInfoPanelLayout.createSequentialGroup()
                        .addComponent(prorogueCheckBox)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(completedTasksInfoPanelLayout.createSequentialGroup()
                        .addGroup(completedTasksInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(completedTasksInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(completedTaskNameTextField)
                            .addComponent(jScrollPane4)
                            .addComponent(jScrollPane5)))
                    .addGroup(completedTasksInfoPanelLayout.createSequentialGroup()
                        .addComponent(prorogueButton, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(finishButton, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(deleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(completedTasksInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                    .addComponent(deleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
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

        selectAlertFileChooser.setCurrentDirectory(new java.io.File("C:\\"));
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "WAV Files", "wav");
            selectAlertFileChooser.setFileFilter(filter);
            selectAlertFileChooser.setDialogTitle("Выбор файла");

            setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
            setTitle("Task Manager");
            setLocationByPlatform(true);
            setName("mainFrame"); // NOI18N
            addWindowListener(new java.awt.event.WindowAdapter() {
                public void windowClosing(java.awt.event.WindowEvent evt) {
                    formWindowClosing(evt);
                }
            });

            jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
            jLabel1.setText("Список задач:");

            tablePanel.setBackground(new java.awt.Color(255, 255, 255));
            tablePanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

            tableScrollPane.setBackground(new java.awt.Color(255, 255, 255));

            taskTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                    "Id", "Название", "Дата и время", "Завершенность"
                }
            ) {
                Class[] types = new Class [] {
                    java.lang.Long.class, java.lang.String.class, java.lang.Object.class, java.lang.String.class
                };
                boolean[] canEdit = new boolean [] {
                    false, false, false, false
                };

                public Class getColumnClass(int columnIndex) {
                    return types [columnIndex];
                }

                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit [columnIndex];
                }
            });
            taskTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
            taskTable.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    taskTableMouseClicked(evt);
                }
                public void mousePressed(java.awt.event.MouseEvent evt) {
                    taskTableMousePressed(evt);
                }
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    taskTableMouseReleased(evt);
                }
            });
            tableScrollPane.setViewportView(taskTable);

            javax.swing.GroupLayout tablePanelLayout = new javax.swing.GroupLayout(tablePanel);
            tablePanel.setLayout(tablePanelLayout);
            tablePanelLayout.setHorizontalGroup(
                tablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(tableScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE)
            );
            tablePanelLayout.setVerticalGroup(
                tablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(tableScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            );

            addButton.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
            addButton.setText("Добавить");
            addButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    addButtonActionPerformed(evt);
                }
            });

            removeButton.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
            removeButton.setText("Удалить ");
            removeButton.setEnabled(false);
            removeButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    removeButtonActionPerformed(evt);
                }
            });

            editButton.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
            editButton.setText("Изменить/просмотреть");
            editButton.setEnabled(false);
            editButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    editButtonActionPerformed(evt);
                }
            });

            jLabel8.setText("Показывать задачи:");

            jLabel9.setText("по приоритету:");

            jLabel10.setText("по активности:");

            priorityComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "все", "высокий", "обычный" }));
            priorityComboBox.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    priorityComboBoxActionPerformed(evt);
                }
            });

            activityComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "активные", "завершённые", "все" }));
            activityComboBox.setSelectedIndex(2);
            activityComboBox.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    activityComboBoxActionPerformed(evt);
                }
            });

            showCompletedDialogButton.setText("Окно активных задач");
            showCompletedDialogButton.setEnabled(false);
            showCompletedDialogButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    showCompletedDialogButtonActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout controlPanelLayout = new javax.swing.GroupLayout(controlPanel);
            controlPanel.setLayout(controlPanelLayout);
            controlPanelLayout.setHorizontalGroup(
                controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(controlPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(addButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(removeButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(editButton, javax.swing.GroupLayout.DEFAULT_SIZE, 264, Short.MAX_VALUE)
                        .addGroup(controlPanelLayout.createSequentialGroup()
                            .addComponent(jLabel8)
                            .addGap(0, 0, Short.MAX_VALUE))
                        .addGroup(controlPanelLayout.createSequentialGroup()
                            .addGroup(controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(priorityComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(activityComboBox, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, controlPanelLayout.createSequentialGroup()
                            .addGap(0, 0, Short.MAX_VALUE)
                            .addComponent(showCompletedDialogButton)))
                    .addContainerGap())
            );
            controlPanelLayout.setVerticalGroup(
                controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(controlPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(addButton)
                    .addGap(18, 18, 18)
                    .addComponent(removeButton)
                    .addGap(18, 18, 18)
                    .addComponent(editButton)
                    .addGap(45, 45, 45)
                    .addComponent(jLabel8)
                    .addGap(18, 18, 18)
                    .addGroup(controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel9)
                        .addComponent(priorityComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel10)
                        .addComponent(activityComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(18, 18, 18)
                    .addComponent(showCompletedDialogButton)
                    .addContainerGap(67, Short.MAX_VALUE))
            );

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                        .addComponent(tablePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(controlPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap())
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addGap(6, 6, 6)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(controlPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(tablePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGap(16, 16, 16))
            );

            pack();
        }// </editor-fold>//GEN-END:initComponents

    /**
     * Выводит форму добавления новой задачи и подготавливает её к вводу
     * @param evt Событие
     */
    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        newTaskDialog.setTitle("Добавить задачу");
        newTaskDialog.setModal(true);
        flagAddOrUpdate = true; // add
        //Разрешаем изменение данных в компонентах для выполненной задачи
        titleTextField.setEditable(true);
        descriptionTextArea.setEditable(true);
        contactsTextArea.setEditable(true);
        jDateChooser.setEnabled(true);
        hourSpinField.setEnabled(true);
        minuteSpinField.setEnabled(true);
        secondSpinField.setEnabled(true);
        highRadioButton.setEnabled(true);
        normalRadioButton.setEnabled(true);
        highRadioButton.setEnabled(true);
        normalRadioButton.setEnabled(true);
        standartSoundCheckBox.setEnabled(true);
        okButton.setEnabled(true);
        titleTextField.setText(null);
        descriptionTextArea.setText(null);
        contactsTextArea.setText(null);
        soundTextField.setText(null);
        soundTextField.setEnabled(false);
        standartSoundCheckBox.setSelected(true);
        soundChooseButton.setEnabled(false);
        //Заполняем текущее время для удобства пользователя
        Calendar currentTime = Calendar.getInstance();        
        hourSpinField.setValue(currentTime.get(Calendar.HOUR_OF_DAY));
        minuteSpinField.setValue(currentTime.get(Calendar.MINUTE));
        secondSpinField.setValue(currentTime.get(Calendar.SECOND));        
        newTaskDialog.setVisible(true);
    }//GEN-LAST:event_addButtonActionPerformed

    /**
     * Скрывает форму добавления новой задачи
     * @param evt Событие
     */
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        newTaskDialog.setVisible(false);
    }//GEN-LAST:event_cancelButtonActionPerformed

    
    /**
     * Изменение содержимого таблицы после изменения задачи.
     * Принимается решение об исключении из таблицы
     * @param task Задача
     * @param indexA    Значение фильтра "По активности"
     * @param indexP    Значение фильтра "По приоритету"
     */
    private void updateTable(Task task, int indexA, int indexP) {
        switch (indexP) {
            case 0: {   //все задачи по приоритету
                switch (indexA) {
                    case 0: {   //Активные задачи
                        if (!task.getWorkOnTask()) {
                            ((DefaultTableModelNotEdit) taskTable.getModel()).
                                    removeRow(taskTable.getSelectedRow());
                        }
                        break;
                    }
                    case 1: {   //завершенные задачи
                        if (!task.isFinished()) {
                            ((DefaultTableModelNotEdit) taskTable.getModel()).
                                    removeRow(taskTable.getSelectedRow());
                        }
                        break;
                    }
                    case 2: {   //все задачи по активности
                        break;
                    }
                }
                break;
            }
            case 1: {   //задачи с высоким приоритетом
                switch (indexA) {
                    case 0: {   //активные задачи
                        if (!task.getWorkOnTask() || !task.isHighPriority()) {
                            ((DefaultTableModelNotEdit) taskTable.getModel()).
                                    removeRow(taskTable.getSelectedRow());
                        }
                        break;
                    }
                    case 1: {   //завершенные задачи
                        if (!task.isFinished() || !task.isHighPriority()) {
                            ((DefaultTableModelNotEdit) taskTable.getModel()).
                                    removeRow(taskTable.getSelectedRow());
                        }
                        break;
                    }
                    case 2: {   //все задачи по активности
                        if (!task.isHighPriority()) {
                            ((DefaultTableModelNotEdit) taskTable.getModel()).
                                    removeRow(taskTable.getSelectedRow());
                        }
                        break;
                    }
                }
                break;
            }
            case 2: {   //Задачи с обычным приоритетом
                switch (indexA) {
                    case 0: {   //активные задачи
                        if (!task.getWorkOnTask() || task.isHighPriority()) {
                            ((DefaultTableModelNotEdit) taskTable.getModel()).
                                    removeRow(taskTable.getSelectedRow());
                        }
                        break;
                    }
                    case 1: {   //завершенные задачи
                        if (!task.isFinished() || task.isHighPriority()) {
                            ((DefaultTableModelNotEdit) taskTable.getModel()).
                                    removeRow(taskTable.getSelectedRow());
                        }
                        break;
                    }
                    case 2: {   //все задачи по активности
                        if (task.isHighPriority()) {
                            ((DefaultTableModelNotEdit) taskTable.getModel()).
                                    removeRow(taskTable.getSelectedRow());
                        }
                        break;
                    }
                }
                break;
            }
        }
    }
    
    /**
     * Обновление существующей либо добавление новой задачи
     * @param evt Событие
     */
    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        GregorianCalendar calendar = new GregorianCalendar();//узнаем текущее время
        //получение выбранной даты от поля выбора даты
        Calendar calendarDef = jDateChooser.getCalendar();
        Task task;
        String finish;
        if (!flagAddOrUpdate) { //update
            calendar.set(calendarDef.get(Calendar.YEAR),
                    calendarDef.get(Calendar.MONTH),
                    calendarDef.get(Calendar.DAY_OF_MONTH),
                    hourSpinField.getValue(), minuteSpinField.getValue(),
                    secondSpinField.getValue());
            jDateChooser.setCalendar(calendar);
            
            long idTask = (long) taskTable.getModel().
                    getValueAt(taskTable.getSelectedRow(), 0);
            
            task = manager.getTask(idTask);
            
//            task = (Task) taskTable.getModel().
//                    getValueAt(taskTable.getSelectedRow(), 0);
            task.setName(titleTextField.getText());
            task.setDescription(descriptionTextArea.getText());
            task.setContacts(contactsTextArea.getText());
            task.setDate(jDateChooser.getCalendar());
            task.setHighPriority(highRadioButton.isSelected());
            //добавить проверку на откладывание задачи после срабатывания по времени
            if (standartSoundCheckBox.isSelected()) {
                task.setSoundFileName(STANDART_SIGNAL);
            } else {
                task.setSoundFileName(soundTextField.getName());
            }
            if (task.isFinished()) {
                finish = "Завершена";
            } else {
                finish = "Не завершена";
            }
            /*Редактируем список задач*/
            DefaultTableModelNotEdit model =
                    (DefaultTableModelNotEdit) taskTable.getModel();
            model.setValueAt(finish, taskTable.getSelectedRow(), 3);
            model.setValueAt(jDateChooser.getCalendar().getTime(),
                    taskTable.getSelectedRow(), 2);
            model.setValueAt(task.toString(), taskTable.getSelectedRow(), 1);
            updateTable(task, activityComboBox.getSelectedIndex(),
                    priorityComboBox.getSelectedIndex());
            taskTable.repaint();
        } else {    //add
//            if (!NameChecking(titleTextField.getText())) {
//                JOptionPane.showMessageDialog(newTaskDialog,
//                        "Задача с таким именем уже существует");
//                return;
//            }
            /*Создаем новую задачу*/
            calendar.set(calendarDef.get(Calendar.YEAR),
                    calendarDef.get(Calendar.MONTH),
                    calendarDef.get(Calendar.DAY_OF_MONTH),
                    hourSpinField.getValue(), minuteSpinField.getValue(),
                    secondSpinField.getValue());
            jDateChooser.setCalendar(calendar);
            String soundFileName;
            if (standartSoundCheckBox.isSelected()) {
                soundFileName = STANDART_SIGNAL;
            } else {
                soundFileName = soundTextField.getText();
            }
            task = new Task(titleTextField.getText(),
                    descriptionTextArea.getText(), contactsTextArea.getText(),
                    jDateChooser.getCalendar(), false,
                    highRadioButton.isSelected(), soundFileName);
            manager.addTask(task);
            addRowTable(task, activityComboBox.getSelectedIndex(),
                    priorityComboBox.getSelectedIndex());
        }
        newTaskDialog.setVisible(false);
    }//GEN-LAST:event_okButtonActionPerformed

    
//    private boolean NameChecking(String name) {
//        for (int i = 0; i < ((ConcreteTaskManager) manager).getLength(); i++) {
//            Task t = manager.getTask(i);
//            if (t.getName().equals(name)) {
//                return false;
//            }
//        }
//        return true;
//    }
    
    /**
     * Блокирует/разблокирует доступ к выбору аудиофайла
     * @param evt Событие
     */
    private void standartSoundCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_standartSoundCheckBoxActionPerformed
        if (!standartSoundCheckBox.isSelected()) {
            soundTextField.setEnabled(true);
            soundChooseButton.setEnabled(true);
        } else {
            soundTextField.setEnabled(false);
            soundTextField.setText(null);
            soundChooseButton.setEnabled(false);
        }
    }//GEN-LAST:event_standartSoundCheckBoxActionPerformed

    /**
     * Позволяет выбрать звуковой файл для оповещения
     * @param evt Событие
     */
    private void soundChooseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_soundChooseButtonActionPerformed
        int ret = selectAlertFileChooser.showDialog(null, "Открыть файл");
        if (ret == JFileChooser.APPROVE_OPTION) {
            if (sound.testFile(selectAlertFileChooser.getSelectedFile())) {
                soundTextField.setText(selectAlertFileChooser.getSelectedFile().
                        getPath());
            } else {
                JOptionPane.showMessageDialog(newTaskDialog,
                        "Неверный формат файла");
            }
        }
    }//GEN-LAST:event_soundChooseButtonActionPerformed

    /**
     * Выводит форму для изменения и/или просмотра задачи и подготавливает её к работе
     * @param evt Событие
     */
    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButtonActionPerformed
        flagAddOrUpdate = false;    //update
        long idTask = (long)taskTable.getModel().
                getValueAt(taskTable.getSelectedRow(), 0);
        Task task = manager.getTask(idTask);
        /*Заполняем компоненты данными из задачи*/
        titleTextField.setText(task.getName());
        descriptionTextArea.setText(task.getDescription());
        contactsTextArea.setText(task.getContacts());
        jDateChooser.setCalendar(task.getDate());
        hourSpinField.setValue(task.getDate().get(Calendar.HOUR_OF_DAY));
        minuteSpinField.setValue(task.getDate().get(Calendar.MINUTE));
        secondSpinField.setValue(task.getDate().get(Calendar.SECOND));
        highRadioButton.setSelected(task.isHighPriority());
        normalRadioButton.setSelected(!task.isHighPriority());
        removeButton.setEnabled(false);
        editButton.setEnabled(false);
        /*Запрещаем изменение данных для выполненной задачи*/
        titleTextField.setEditable(!task.isFinished());
        descriptionTextArea.setEditable(!task.isFinished());
        contactsTextArea.setEditable(!task.isFinished());
        jDateChooser.setEnabled(!task.isFinished());
        hourSpinField.setEnabled(!task.isFinished());
        minuteSpinField.setEnabled(!task.isFinished());
        secondSpinField.setEnabled(!task.isFinished());
        highRadioButton.setEnabled(!task.isFinished());
        normalRadioButton.setEnabled(!task.isFinished());
        standartSoundCheckBox.setEnabled(!task.isFinished());
        okButton.setEnabled(!task.isFinished());
        newTaskDialog.setVisible(true);
    }//GEN-LAST:event_editButtonActionPerformed

    /**
     * Удаляет задачу после подтверждения пользователя
     * @param evt Событие
     */
    private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeButtonActionPerformed
        if (JOptionPane.showConfirmDialog(this,
                "Вы уверены, что хотите удалить эту задачу?", null,
                JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE)
                == JOptionPane.YES_OPTION) {
            long idTask = (long)taskTable.
                    getValueAt(taskTable.getSelectedRow(), 0);
            Task task = manager.getTask(idTask);
            /*Удаляем задачу из таблицы с задачами*/
            ((DefaultTableModelNotEdit) taskTable.getModel()).
                    removeRow(taskTable.getSelectedRow());
            /*Удаляем задачу из таблицы с выполненными заданиями*/
            for (int k = 0; k < completedTasksList.getModel().getSize(); k++) {
                if (((DefaultListModel<Task>) completedTasksList.getModel()).
                        get(k).equals(task)) {
                    ((DefaultListModel<Task>) completedTasksList.getModel()).
                            removeElementAt(k);
                }
            }
            /*Удаляем задачу из журнала задач*/
            manager.removeTask(idTask);
            
            removeButton.setEnabled(false);
            editButton.setEnabled(false);
            if (((DefaultListModel<Task>) completedTasksList.getModel()).
                    isEmpty()) {
                trayIcon.setToolTip("Нет готовых заданий");
                prorogueCheckBox.setSelected(false);
                evt.setSource(prorogueCheckBox);
                prorogueCheckBoxActionPerformed(evt);
                showCompletedDialogButton.setEnabled(false);
            }
        } else {
            removeButton.setEnabled(false);
            editButton.setEnabled(false);
        }

    }//GEN-LAST:event_removeButtonActionPerformed

    
    private void taskTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_taskTableMouseClicked
        if ((!removeButton.isEnabled())
                && (evt.getButton() == java.awt.event.MouseEvent.BUTTON1)) {
            removeButton.setEnabled(true);
            editButton.setEnabled(true);
        }
    }//GEN-LAST:event_taskTableMouseClicked

    /**
     * Блокирует/разблокирует доступ к изменению времени задачи
     * @param evt Событие
     */
    private void prorogueCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prorogueCheckBoxActionPerformed
        prorogueButton.setEnabled(prorogueCheckBox.isSelected());
        dateChooserRadioButton.setEnabled(prorogueCheckBox.isSelected());
        dateComboBoxRadioButton.setEnabled(prorogueCheckBox.isSelected());
        jDateChooser1.setEnabled(prorogueCheckBox.isSelected());
        prorogueHourSpinField.setEnabled(prorogueCheckBox.isSelected());
        prorogueMinuteSpinField.setEnabled(prorogueCheckBox.isSelected());
        prorogueSecondSpinField.setEnabled(prorogueCheckBox.isSelected());
    }//GEN-LAST:event_prorogueCheckBoxActionPerformed

    /**
     * Разрешает выбор произвольных даты/времени
     * @param evt Событие
     */
    private void dateChooserRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dateChooserRadioButtonActionPerformed
        dateComboBox.setEnabled(false);
        jDateChooser1.setEnabled(true);
        prorogueHourSpinField.setEnabled(true);
        prorogueMinuteSpinField.setEnabled(true);
        prorogueSecondSpinField.setEnabled(true);
    }//GEN-LAST:event_dateChooserRadioButtonActionPerformed

    /**
     * Разрешает отложить дату/время на заранее заданные промежутки
     * @param evt Событие
     */
    private void dateComboBoxRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dateComboBoxRadioButtonActionPerformed
        dateComboBox.setEnabled(true);
        jDateChooser1.setEnabled(false);
        prorogueHourSpinField.setEnabled(false);
        prorogueMinuteSpinField.setEnabled(false);
        prorogueSecondSpinField.setEnabled(false);
    }//GEN-LAST:event_dateComboBoxRadioButtonActionPerformed

    /**
     * Переводит дату/время задачи вперёд
     * @param evt Событие
     */
    private void prorogueButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prorogueButtonActionPerformed
        /*Переводим дату оповещения вперёд*/
        GregorianCalendar calendar = new GregorianCalendar();
        Calendar calendarDef = jDateChooser1.getCalendar();
        ((Task) completedTasksList.getSelectedValue()).
                setWorkOnTask(false);
        if (dateChooserRadioButton.isSelected()) {
            calendar.set(calendarDef.get(Calendar.YEAR),
                    calendarDef.get(Calendar.MONTH),
                    calendarDef.get(Calendar.DAY_OF_MONTH),
                    prorogueHourSpinField.getValue(),
                    prorogueMinuteSpinField.getValue(),
                    prorogueSecondSpinField.getValue());
            if (calendar.before(Calendar.getInstance())) {
                JOptionPane.showMessageDialog(null,
                        "Нельзя вводить дату и время меньше текущей!");
            } else {
                jDateChooser1.setCalendar(calendar);
                ((Task) completedTasksList.getSelectedValue()).
                        setName(completedTaskNameTextField.getText());
                ((Task) completedTasksList.getSelectedValue()).
                        setDescription(completedTaskDescriptionTextArea.
                        getText());
                ((Task) completedTasksList.getSelectedValue()).
                        setContacts(completedTaskContactsTextArea.getText());
                ((Task) completedTasksList.getSelectedValue()).
                        setDate(jDateChooser1.getCalendar());
                ((Task) completedTasksList.getSelectedValue()).
                        setWorkOnTask(false);
                ((DefaultListModel<Task>) completedTasksList.getModel()).
                        removeElementAt(completedTasksList.getSelectedIndex());
            }
        } else {
            Task task = ((Task) completedTasksList.getSelectedValue());
            switch (dateComboBox.getSelectedIndex()) {
                case 0: {
                    task.getDate().set(Calendar.HOUR_OF_DAY,
                            task.getDate().get(Calendar.HOUR_OF_DAY) + 1);
                    break;
                }
                case 1: {
                    task.getDate().set(Calendar.DAY_OF_MONTH,
                            task.getDate().get(Calendar.DAY_OF_MONTH) + 1);
                    break;
                }
                case 2: {
                    task.getDate().set(Calendar.DAY_OF_MONTH,
                            task.getDate().get(Calendar.DAY_OF_MONTH) + 7);
                    break;
                }
                case 3: {
                    task.getDate().set(Calendar.MONTH,
                            task.getDate().get(Calendar.MONTH) + 1);
                    break;
                }
            }
            ((Task) completedTasksList.getSelectedValue()).
                    setWorkOnTask(false);
            ((DefaultListModel<Task>) completedTasksList.getModel()).
                    removeElementAt(completedTasksList.getSelectedIndex());
            taskTable.repaint();
        }
        if (((DefaultListModel<Task>) completedTasksList.getModel()).isEmpty()) {
            completedTasksDialog.setVisible(false);
            prorogueCheckBox.setSelected(false);
            evt.setSource(prorogueCheckBox);
            prorogueCheckBoxActionPerformed(evt);
            trayIcon.setToolTip("Нет готовых заданий");
            showCompletedDialogButton.setEnabled(false);
        }
        changingTable(activityComboBox.getSelectedIndex(),
                priorityComboBox.getSelectedIndex());
    }//GEN-LAST:event_prorogueButtonActionPerformed

    /**
     * Изменяет статус задачи на завершенный.
     * @param evt 
     */
    private void finishButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_finishButtonActionPerformed
        ((Task) completedTasksList.getSelectedValue()).setFinished(true);
        ((Task) completedTasksList.getSelectedValue()).setWorkOnTask(false);
        ((DefaultListModel<Task>) completedTasksList.getModel()).
                removeElementAt(completedTasksList.getSelectedIndex());
        if (((DefaultListModel<Task>) completedTasksList.getModel()).isEmpty()) {
            completedTasksDialog.setVisible(false);
            prorogueCheckBox.setSelected(false);
            evt.setSource(prorogueCheckBox);
            prorogueCheckBoxActionPerformed(evt);
            trayIcon.setToolTip("Нет готовых заданий");
            showCompletedDialogButton.setEnabled(false);
        }
        changingTable(activityComboBox.getSelectedIndex(),
                priorityComboBox.getSelectedIndex());
    }//GEN-LAST:event_finishButtonActionPerformed

    /**
     * Удаляет задачу из окна completedTaskDialog
     * @param evt Событие
     */
    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        if (JOptionPane.showConfirmDialog(this,
                "Вы уверены, что хотите удалить эту задачу?", null,
                JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE)
                == JOptionPane.YES_OPTION) {
            Task t = (Task) completedTasksList.getSelectedValue();
//            int i = 0;
//            while ((i < ((ConcreteTaskManager) manager).getLength())
//                    && (manager.getTask(i) != t)) {
//                i++;
//            }
            ((DefaultListModel<Task>) completedTasksList.getModel()).
                    removeElementAt(completedTasksList.getSelectedIndex());
            
            if (((DefaultListModel<Task>) completedTasksList.getModel()).
                    isEmpty()) {
                completedTasksDialog.setVisible(false);
            }
            
            for (int j = 0; j < taskTable.getRowCount(); j++) {
                if ((long)taskTable.getModel().getValueAt(j, 0) == t.getId()) {
                    ((DefaultTableModelNotEdit) taskTable.getModel()).
                            removeRow(j);
                    break;
                }
            }
            manager.removeTask(t.getId());
            if (((DefaultListModel<Task>) completedTasksList.getModel()).
                    isEmpty()) {
                completedTasksDialog.setVisible(false);
                prorogueCheckBox.setSelected(false);
                evt.setSource(prorogueCheckBox);
                prorogueCheckBoxActionPerformed(evt);
                trayIcon.setToolTip("Нет готовых заданий");
                showCompletedDialogButton.setEnabled(false);
            }
            taskTable.repaint();
        }

    }//GEN-LAST:event_deleteButtonActionPerformed

    /**
     * Выводит информацию о выполненной задаче
     * @param evt Событие
     */
    private void completedTasksListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_completedTasksListMouseClicked
        if (evt.getButton() == MouseEvent.BUTTON1) {
            Task task = (Task) completedTasksList.getModel().
                    getElementAt(completedTasksList.getSelectedIndex());
            completedTaskNameTextField.setText(task.getName());
            completedTaskDescriptionTextArea.setText(task.getDescription());
            completedTaskContactsTextArea.setText(task.getContacts());
        }
    }//GEN-LAST:event_completedTasksListMouseClicked

    /**
     * Изменяет фильтр приоритета
     * @param evt Событие
     */
    private void priorityComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_priorityComboBoxActionPerformed
        changingTable(activityComboBox.getSelectedIndex(),
                priorityComboBox.getSelectedIndex());
        removeButton.setEnabled(false);
        editButton.setEnabled(false);
    }//GEN-LAST:event_priorityComboBoxActionPerformed

    /**
     * Изменение таблицы в зависимости от выбранных фильтров
     * @param indexA Значение фильтра "По активности"
     * @param indexP Значение фильтра "По приоритету"
     */
    private void changingTable(int indexA, int indexP) {
        Task[] array;
        Object[] column = {"Id","Название", "Дата и время", "Завершенность"};
        taskTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tableScrollPane.setViewportView(taskTable);
        switch (indexP) {
            case 0: {   //все задачи по приоритету
                switch (indexA) {
                    case 0: {   //Активные задачи
                        array = manager.getWorkOnTasks();
                        DefaultTableModelNotEdit model =
                                new DefaultTableModelNotEdit();
                        model.setColumnIdentifiers(column);
                        if (array != null) {
                            for (Task t : array) {
                                model.addRow(new Object[]{t.getId(), t.toString(), t.getDate().
                                    getTime(), "Не завершена"});
                            }
                        }
                        taskTable.setModel(model);
                        break;
                    }
                    case 1: {   //завершенные задачи
                        array = manager.getFinishedTasks();
                        DefaultTableModelNotEdit model =
                                new DefaultTableModelNotEdit();
                        model.setColumnIdentifiers(column);
                        if (array != null) {
                            for (Task t : array) {
                                model.addRow(new Object[]{t.getId(), t.toString(), t.getDate().
                                    getTime(), "Завершена"});
                            }
                        }
                        taskTable.setModel(model);
                        break;
                    }
                    case 2: {   //все задачи по активности
                        DefaultTableModelNotEdit model =
                                new DefaultTableModelNotEdit();
                        model.setColumnIdentifiers(column);
                        Collection<Task> col = manager.getCollection();
                        if (col != null) {
                        for (Task task: col) {
                            if (task.isFinished()) {
                                model.addRow(new Object[]{task.getId(),task.toString(), task.getDate().
                                            getTime(), "Завершена"});
                            } else {
                                model.addRow(new Object[]{task.getId(), task.toString(), task.getDate().
                                            getTime(), "Не завершена"});
                            }
                        }
                        }
                        taskTable.setModel(model);
                        break;
                    }
                }
                break;
            }
            case 1: {   //задачи с высоким приоритетом
                switch (indexA) {
                    case 0: {   //активные задачи
                        DefaultTableModelNotEdit model =
                                new DefaultTableModelNotEdit();
                        model.setColumnIdentifiers(column);
                        array = manager.getHighPrioritedTasks();
                        if (array != null) {
                            for (Task task : array) {
                                if (task.getWorkOnTask()) {
                                    model.addRow(new Object[]{task.getId(), task.toString(), task.getDate().
                                        getTime(), "Не завершена"});
                                }
                            }
                        }
                        taskTable.setModel(model);
                        break;
                    }
                    case 1: {   //завершенные задачи
                        DefaultTableModelNotEdit model =
                                new DefaultTableModelNotEdit();
                        model.setColumnIdentifiers(column);
                        array = manager.getHighPrioritedTasks();
                        if (array != null) {
                            for (Task task : manager.getHighPrioritedTasks()) {
                                if (task.isFinished()) {
                                    model.addRow(new Object[]{task.getId(), task.toString(), task.getDate().
                                        getTime(), "Завершена"});
                                }
                            }
                        }
                        taskTable.setModel(model);
                        break;
                    }
                    case 2: {   //все задачи по активности
                        DefaultTableModelNotEdit model =
                                new DefaultTableModelNotEdit();
                        model.setColumnIdentifiers(column);
                        array = manager.getHighPrioritedTasks();
                        if (array != null) {
                            for (Task task : array) {
                                if (task.isFinished()) {
                                    model.addRow(new Object[]{task.getId(), task.toString(),
                                        task.getDate().getTime(),
                                        "Завершена"});
                                } else {
                                    model.addRow(new Object[]{task.getId(),task.toString(),
                                        task.getDate().getTime(),
                                        "Не завершена"});
                                }

                            }
                        }
                        taskTable.setModel(model);
                        break;
                    }
                }
                break;
            }
            case 2: {   //Задачи с обычным приоритетом
                switch (indexA) {
                    case 0: {   //активные задачи
                        DefaultTableModelNotEdit model =
                                new DefaultTableModelNotEdit();
                        model.setColumnIdentifiers(column);
                        array = manager.getNormalPrioritedTasks();
                        if(array != null){
                            for (Task task: array) {
                                if (task.getWorkOnTask()) {
                                    model.addRow(new Object[]{task.getId(), task.toString(),
                                        task.getDate().getTime(), "Не завершена"});
                                }
                            }
                        }
                        taskTable.setModel(model);
                        break;
                    }
                    case 1: {   //завершенные задачи
                        DefaultTableModelNotEdit model =
                                new DefaultTableModelNotEdit();
                        model.setColumnIdentifiers(column);
                        array = manager.getNormalPrioritedTasks();
                        if (array != null) {
                            for (Task task : array) {
                                if (task.isFinished()) {
                                    model.addRow(new Object[]{task.getId(), task.toString(), 
                                        task.getDate().getTime(), "Завершена"});
                                }
                            }
                        }
                        taskTable.setModel(model);
                        break;
                    }
                    case 2: {   //все задачи по активности
                        DefaultTableModelNotEdit model =
                                new DefaultTableModelNotEdit();
                        model.setColumnIdentifiers(column);
                        array = manager.getNormalPrioritedTasks();
                        if (array != null) {
                            for (Task task : array) {
                                if (task.isFinished()) {
                                    model.addRow(new Object[]{task.getId(),
                                        task.toString(), task.getDate().getTime(),
                                        "Завершена"});
                                } else {
                                    model.addRow(new Object[]{task.getId(),
                                        task.toString(), task.getDate().getTime(),
                                        "Не завершена"});
                                }
                            }
                        }
                        taskTable.setModel(model);
                        break;
                    }
                }
                break;
            }
        }
        for (int i = 0; i < taskTable.getColumnCount(); i++) {
            taskTable.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
        tablePanel.setPreferredSize(
                new Dimension(this.getWidth() * 2 / 3, this.getHeight()));
        taskTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tableScrollPane.setViewportView(taskTable);
        taskTable.setEditingRow(-1);
    }
    
    /**
     * Добавление строки с задачей
     * @param t Задача
     * @param indexA Значение фильтра "По активности"
     * @param indexP Значение фильтра "По приоритету"
     */
    private void addRowTable(Task t, int indexA, int indexP) {
        switch (indexP) {
            case 0: {   //все задачи по приоритету
                switch (indexA) {
                    case 0: {   //Активные задачи
                        if (t.getWorkOnTask()) {
                            ((DefaultTableModelNotEdit) taskTable.getModel()).
                                    addRow(new Object[]{t.getId(), t.toString(), t.getDate().
                                        getTime(), "Не завершена"});
                        }
                        break;
                    }
                    case 1: {   //завершенные задачи
                        if (t.isFinished()) {
                            ((DefaultTableModelNotEdit) taskTable.getModel()).
                                    addRow(new Object[]{t.getId(), t.toString(), t.getDate().
                                        getTime(), "Завершена"});
                        }
                        break;
                    }
                    case 2: {   //все задачи по активности
                        if (t.isFinished()) {
                            ((DefaultTableModelNotEdit) taskTable.getModel()).
                                    addRow(new Object[]{t.getId(), t.toString(), t.getDate().
                                        getTime(), "Завершена"});
                        } else {
                            ((DefaultTableModelNotEdit) taskTable.getModel()).
                                    addRow(new Object[]{t.getId(), t.toString(), t.getDate().
                                        getTime(), "Не завершена"});
                        }
                        break;
                    }
                }
                break;
            }
            case 1: {   //задачи с высоким приоритетом
                switch (indexA) {
                    case 0: {   //активные задачи
                        if (t.getWorkOnTask() && t.isHighPriority()) {
                            ((DefaultTableModelNotEdit) taskTable.getModel()).
                                    addRow(new Object[]{t.getId(), t.toString(), t.getDate().
                                        getTime(), "Не завершена"});
                        }
                        break;
                    }
                    case 1: {   //завершенные задачи
                        if (t.isFinished() && t.isHighPriority()) {
                            ((DefaultTableModelNotEdit) taskTable.getModel()).
                                    addRow(new Object[]{t.getId(), t.toString(), t.getDate().
                                        getTime(), "Завершена"});
                        }
                        break;
                    }
                    case 2: {   //все задачи по активности
                        if (t.isHighPriority()) {
                            if (t.isFinished()) {
                                ((DefaultTableModelNotEdit) taskTable.getModel()).
                                        addRow(new Object[]{t.getId(), t.toString(), t.getDate().
                                            getTime(), "Завершена"});
                            } else {
                                ((DefaultTableModelNotEdit) taskTable.getModel()).
                                        addRow(new Object[]{t.getId(), t.toString(), t.getDate().
                                            getTime(), "Не завершена"});
                            }
                        }

                        break;
                    }
                }
                break;
            }
            case 2: {   //Задачи с обычным приоритетом
                switch (indexA) {
                    case 0: {   //активные задачи
                        if (t.getWorkOnTask() && !t.isHighPriority()) {
                            ((DefaultTableModelNotEdit) taskTable.getModel()).
                                    addRow(new Object[]{t.getId(), t.toString(), t.getDate().
                                        getTime(), "Не завершена"});
                        }
                        break;
                    }
                    case 1: {   //завершенные задачи
                        if (t.isFinished() && !t.isHighPriority()) {
                            ((DefaultTableModelNotEdit) taskTable.getModel()).
                                    addRow(new Object[]{t.getId(), t.toString(), t.getDate().
                                        getTime(), "Завершена"});
                        }
                        break;
                    }
                    case 2: {   //все задачи по активности
                        if (!t.isHighPriority()) {
                            if (t.isFinished()) {
                                ((DefaultTableModelNotEdit) taskTable.getModel()).
                                        addRow(new Object[]{t.getId(), t.toString(), t.getDate().
                                            getTime(), "Завершена"});
                            } else {
                                ((DefaultTableModelNotEdit) taskTable.getModel()).
                                        addRow(new Object[]{t.getId(), t.toString(), t.getDate().
                                            getTime(), "Не авершена"});
                            }
                        }
                        break;
                    }
                }
                break;
            }
        }
    }
    /**
     * Изменяет фильтр активности
     * @param evt Событие
     */
    private void activityComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_activityComboBoxActionPerformed
        changingTable(activityComboBox.getSelectedIndex(),
                priorityComboBox.getSelectedIndex());
        removeButton.setEnabled(false);
        editButton.setEnabled(false);
    }//GEN-LAST:event_activityComboBoxActionPerformed

    /**
     * Сохраняет журнал задач перед закрытием
     * @param evt Событие
     */
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        try {
            manager.saveTasks();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Не удалось сохранить журнал заданий");
        }
        this.dispose();
    }//GEN-LAST:event_formWindowClosing

    /**
     * Выводит окно выполненных задач
     * @param evt Событие
     */
    private void showCompletedDialogButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showCompletedDialogButtonActionPerformed
        completedTasksDialog.setVisible(true);
    }//GEN-LAST:event_showCompletedDialogButtonActionPerformed

    /**
     * Выводит всплывающее окно при клике по таблице с задачами
     * @param evt Событие мыши
     */
    private void taskTableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_taskTableMousePressed
        if (evt.getButton() == MouseEvent.BUTTON3) {
            Point point = evt.getPoint();
            int column = taskTable.columnAtPoint(point);
            int row = taskTable.rowAtPoint(point);
            if (column != -1 && row != -1) {
                taskTable.setRowSelectionInterval(row, row);
                taskTable.setColumnSelectionInterval(column, column);
                if (!removeButton.isEnabled()) {
                    removeButton.setEnabled(true);
                    editButton.setEnabled(true);
                }
                if (evt.isPopupTrigger()) {
                    popup.show(evt.getComponent(), evt.getX(), evt.getY());
                }
            }
        }
    }//GEN-LAST:event_taskTableMousePressed

    private void taskTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_taskTableMouseReleased
        if (evt.isPopupTrigger()) {
            popup.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_taskTableMouseReleased

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox activityComboBox;
    private javax.swing.JButton addButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JTextArea completedTaskContactsTextArea;
    private javax.swing.JTextArea completedTaskDescriptionTextArea;
    private javax.swing.JTextField completedTaskNameTextField;
    private javax.swing.JDialog completedTasksDialog;
    private javax.swing.JPanel completedTasksInfoPanel;
    private javax.swing.JList completedTasksList;
    private javax.swing.JPanel completedTasksListPanel;
    private javax.swing.JTextArea contactsTextArea;
    private javax.swing.JPanel controlPanel;
    private javax.swing.JRadioButton dateChooserRadioButton;
    private javax.swing.JComboBox dateComboBox;
    private javax.swing.JRadioButton dateComboBoxRadioButton;
    private javax.swing.ButtonGroup datebuttonGroup;
    private javax.swing.JButton deleteButton;
    private javax.swing.JTextArea descriptionTextArea;
    private javax.swing.JButton editButton;
    private javax.swing.JButton finishButton;
    private javax.swing.JRadioButton highRadioButton;
    private com.toedter.components.JSpinField hourSpinField;
    private com.toedter.calendar.JDateChooser jDateChooser;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private com.toedter.components.JSpinField minuteSpinField;
    private javax.swing.JDialog newTaskDialog;
    private javax.swing.JRadioButton normalRadioButton;
    private javax.swing.JButton okButton;
    private javax.swing.JComboBox priorityComboBox;
    private javax.swing.JLabel priorityLabel;
    private javax.swing.ButtonGroup prioritybuttonGroup;
    private javax.swing.JButton prorogueButton;
    private javax.swing.JCheckBox prorogueCheckBox;
    private com.toedter.components.JSpinField prorogueHourSpinField;
    private com.toedter.components.JSpinField prorogueMinuteSpinField;
    private com.toedter.components.JSpinField prorogueSecondSpinField;
    private javax.swing.JButton removeButton;
    private com.toedter.components.JSpinField secondSpinField;
    private javax.swing.JFileChooser selectAlertFileChooser;
    private javax.swing.JButton showCompletedDialogButton;
    private javax.swing.JButton soundChooseButton;
    private javax.swing.JTextField soundTextField;
    private javax.swing.JCheckBox standartSoundCheckBox;
    private javax.swing.JPanel tablePanel;
    private javax.swing.JScrollPane tableScrollPane;
    private javax.swing.JTable taskTable;
    private javax.swing.JTextField titleTextField;
    // End of variables declaration//GEN-END:variables

    /**
     * Получает список выполненных задач от системы оповещения.
     * Воспроизодит звуковой файл, изменяет трей либо выводит окно выполненных
     * задач.
     * @param tasks Массив выполненных задач
     */
    @Override
    public void update(Task[] tasks) {
        completedListModel = (DefaultListModel<Task>) completedTasksList.getModel();
        for (Task t : tasks) {
            completedListModel.addElement(t);
        }
        sound.playFile(new File(tasks[0].getSoundFileName()));
        sound.start();
        if (completedTasksList.getSelectedIndex() == -1) {
            completedTasksList.setSelectedIndex(0);
            Task t = (Task) completedTasksList.getSelectedValue();
            completedTaskNameTextField.setText(t.getName());
            completedTaskContactsTextArea.setText(t.getContacts());
            completedTaskDescriptionTextArea.setText(t.getDescription());
            trayIcon.setToolTip(completedListModel.getSize()
                    + " готовых(ое) задания(е)");
        }
        if (!this.isVisible()) {//главное окно свернуто и есть значок в трее
            try {
                this.removeTrayIcon();
                trayIcon.setImage(ImageIO.read(
                        new File(COMPLETED_ICON)));
                trayIcon.setToolTip(completedListModel.getSize()
                        + " готовых(ое) задания(е)");
                this.addTrayIcon();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null,
                        "Не могу загрузить иконку для трея");
            }
        } else {
            trayIcon.setToolTip(completedListModel.getSize()
                    + " готовых(ое) задания(е)");
            completedTasksDialog.setVisible(true);
        }
        showCompletedDialogButton.setEnabled(true);

    }

    /**
     * Удаляет иконку из трея
     */
    private void removeTrayIcon() {
        sysTray.remove(trayIcon);
    }

    /**
     * Добавляет иконку в трей
     */
    public void addTrayIcon() {
        try {
            sysTray.add(trayIcon);
            trayIcon.displayMessage("TaskManager",
                    "Window minimised to tray, double clicktoshow",
                    TrayIcon.MessageType.INFO);
        } catch (AWTException ex) {
            JOptionPane.showMessageDialog(this,
                    "Не удалось добавить иконку трея");
        }
    }
}
