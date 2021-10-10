package um.tds.vista;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

public class PanelExplorar extends JPanel {
	
	private JTextField txtInterprete_1;
	private JTextField txtTitulo_1;
	private JTextField txtEstilo_1;
	private JTable table_3;
	GridBagConstraints gbc_panelCentralExplorar;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	
	
	public PanelExplorar() {
		initialize();
	}
	
	
	public void initialize() {
		JPanel panelCentralExplorar = new JPanel();
		panelCentralExplorar.setBackground(new Color(173, 216, 230));
		gbc_panelCentralExplorar = new GridBagConstraints();
		gbc_panelCentralExplorar.gridheight = 4;
		gbc_panelCentralExplorar.gridwidth = 5;
		gbc_panelCentralExplorar.fill = GridBagConstraints.BOTH;
		gbc_panelCentralExplorar.gridx = 0;
		gbc_panelCentralExplorar.gridy = 0;
		GridBagLayout gbl_panelCentralExplorar = new GridBagLayout();
		gbl_panelCentralExplorar.columnWidths = new int[]{0, 0, 45, 0, 0, 0};
		gbl_panelCentralExplorar.rowHeights = new int[]{0, 49, 0, 0, 30, 0, 0};
		gbl_panelCentralExplorar.columnWeights = new double[]{1.0, 1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
		gbl_panelCentralExplorar.rowWeights = new double[]{1.0, 0.0, 0.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
		panelCentralExplorar.setLayout(gbl_panelCentralExplorar);
		
		JPanel panelSuperior = new JPanel();
		panelSuperior.setBackground(new Color(173, 216, 230));
		panelSuperior.setBorder(UIManager.getBorder("FileChooser.listViewBorder"));
		GridBagConstraints gbc_panelSuperior = new GridBagConstraints();
		gbc_panelSuperior.gridwidth = 3;
		gbc_panelSuperior.insets = new Insets(0, 0, 5, 5);
		gbc_panelSuperior.fill = GridBagConstraints.BOTH;
		gbc_panelSuperior.gridx = 1;
		gbc_panelSuperior.gridy = 1;
		panelCentralExplorar.add(panelSuperior, gbc_panelSuperior);
		
		txtInterprete_1 = new JTextField();
		txtInterprete_1.setText("Interprete");
		panelSuperior.add(txtInterprete_1);
		txtInterprete_1.setColumns(10);
		
		txtTitulo_1 = new JTextField();
		txtTitulo_1.setText("Titulo");
		panelSuperior.add(txtTitulo_1);
		txtTitulo_1.setColumns(10);
		
		txtEstilo_1 = new JTextField();
		txtEstilo_1.setText("Estilo");
		panelSuperior.add(txtEstilo_1);
		txtEstilo_1.setColumns(10);
		
		JComboBox comboBox = new JComboBox();
		panelSuperior.add(comboBox);
		
		JPanel panelCentro = new JPanel();
		panelCentro.setBorder(UIManager.getBorder("FormattedTextField.border"));
		panelCentro.setBackground(new Color(173, 216, 230));
		GridBagConstraints gbc_panelCentro = new GridBagConstraints();
		gbc_panelCentro.insets = new Insets(0, 0, 5, 5);
		gbc_panelCentro.fill = GridBagConstraints.BOTH;
		gbc_panelCentro.gridx = 2;
		gbc_panelCentro.gridy = 2;
		panelCentralExplorar.add(panelCentro, gbc_panelCentro);
		
		JButton btnBuscar_1 = new JButton("Buscar");
		panelCentro.add(btnBuscar_1);
		
		JButton btnCancelar_1 = new JButton("Cancelar");
		panelCentro.add(btnCancelar_1);
		
		JPanel panelTabla = new JPanel();
		panelTabla.setBackground(new Color(173, 216, 230));
		panelTabla.setBorder(UIManager.getBorder("Menu.border"));
		GridBagConstraints gbc_panelTabla = new GridBagConstraints();
		gbc_panelTabla.insets = new Insets(0, 0, 5, 5);
		gbc_panelTabla.fill = GridBagConstraints.BOTH;
		gbc_panelTabla.gridx = 2;
		gbc_panelTabla.gridy = 3;
		panelCentralExplorar.add(panelTabla, gbc_panelTabla);
		
		table_3 = new JTable();
		table_3.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
			},
			new String[] {
				"New column", "New column"
			}
		));
		panelTabla.add(table_3);
		
		JPanel panelBotones = new JPanel();
		panelBotones.setBorder(UIManager.getBorder("FileChooser.listViewBorder"));
		panelBotones.setBackground(new Color(173, 216, 230));
		GridBagConstraints gbc_panelBotones = new GridBagConstraints();
		gbc_panelBotones.gridwidth = 3;
		gbc_panelBotones.insets = new Insets(0, 0, 5, 5);
		gbc_panelBotones.fill = GridBagConstraints.VERTICAL;
		gbc_panelBotones.gridx = 1;
		gbc_panelBotones.gridy = 4;
		panelCentralExplorar.add(panelBotones, gbc_panelBotones);
		
		JButton button = new JButton("");
		button.setPreferredSize(new Dimension(50, 50));
		button.setMinimumSize(new Dimension(50, 50));
		button.setMaximumSize(new Dimension(50, 50));
		panelBotones.add(button);
		
		JButton button_1 = new JButton("");
		button_1.setPreferredSize(new Dimension(50, 50));
		panelBotones.add(button_1);
		
		JButton button_2 = new JButton("");
		button_2.setPreferredSize(new Dimension(50, 50));
		panelBotones.add(button_2);
		
		JButton button_3 = new JButton("");
		button_3.setPreferredSize(new Dimension(50, 50));
		panelBotones.add(button_3);
	}

}
