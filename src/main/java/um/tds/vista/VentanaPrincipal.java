package um.tds.vista;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.ScrollPane;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.SystemColor;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import java.awt.ComponentOrientation;
import javax.swing.ImageIcon;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Color;
import javax.swing.JTable;
import java.awt.GridLayout;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import com.itextpdf.text.DocumentException;

import um.tds.dominio.ListaCanciones;
import um.tds.dominio.Usuario;
import um.tds.controlador.Controlador;
import um.tds.dao.DAOException;
import um.tds.dominio.Cancion;
import um.tds.dominio.Descuento;

import javax.swing.JList;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;

import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import javax.swing.JScrollPane;
import java.awt.Rectangle;
import pulsador.Luz;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

public class VentanaPrincipal {

	private JFrame frmVentanaPrincipal;
	private JTable table;
	private JTextField txtNombreLista;
	private JTextField txtInterprete;
	private JTextField txtTitulo;
	private JTable table_1;
	private JTable table_2;
	private JTextField txtInterprete_1;
	private JTextField txtTitulo_1;
	private JTable table_3;
	private JTable table_4;
	private JPanel panelCentral;
	private String ruta_fichero;
	private boolean playlistRepe;
	private int indiceLastSongReproducida;
	private boolean descuentoEscogido = false;

	public VentanaPrincipal() throws DAOException, ParseException {
		initialize();
	}

	public void mostrarVentana() {
		frmVentanaPrincipal.setLocationRelativeTo(null);
		frmVentanaPrincipal.setVisible(true);
	}

	public void initialize() throws DAOException, ParseException {
		frmVentanaPrincipal = new JFrame();
		frmVentanaPrincipal.setSize(new Dimension(900, 600));
		frmVentanaPrincipal.setPreferredSize(new Dimension(900, 600));
		frmVentanaPrincipal.setBounds(new Rectangle(600, 300, 900, 600));
		frmVentanaPrincipal.setBounds(630, 280, 858, 509);
		frmVentanaPrincipal.getContentPane().setBackground(new Color(100, 149, 237));
		frmVentanaPrincipal.setTitle("Music App - Ventana Principal");
		frmVentanaPrincipal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmVentanaPrincipal.setIconImage(
				Toolkit.getDefaultToolkit().getImage(VentanaPrincipal.class.getResource("/um/tds/iconos/logo1.png")));

		// PANEL PRINCIPAL
		crearPanelPrincipal();

		// PANEL SUPERIOR
		crearPanelSuperior();

		// PANEL IZQUIERDO
		crearPanelIzquierdo();

		// PANEL CENTRAL
		crearPanelCentral(crearCentroReciente(), crearCentroExplorar(), crearCentroMisListas(), crearCentroNuevaLista(),
				crearCentroMasRepro(), crearCentroPagoPremium());

		frmVentanaPrincipal.pack();
	}

	ArrayList<Cancion> cancionesNoAnadidas = new ArrayList<Cancion>();
	ArrayList<Cancion> cancionesAnadidas = new ArrayList<Cancion>();
	ArrayList<Cancion> cancionesExploradas = new ArrayList<Cancion>();
	ArrayList<Cancion> cancionesRecientes = Controlador.getUnicaInstancia().getRecientes();
	ArrayList<Cancion> cancionesListaConcreta = new ArrayList<Cancion>();
	ArrayList<Cancion> masReproducidasUsuario = new ArrayList<Cancion>();
	ArrayList<Cancion> masReproducidasSistema = new ArrayList<Cancion>();

	private JPanel crearCentroPagoPremium() throws ParseException {
		JPanel panelCentralPremium = new JPanel();
		panelCentralPremium.setBackground(new Color(173, 216, 230));
		GridBagLayout gbl_panelCentralPremium = new GridBagLayout();
		gbl_panelCentralPremium.columnWidths = new int[] { 0, 0, 0, 0, 0, 0, 0 };
		gbl_panelCentralPremium.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 53, 0 };
		gbl_panelCentralPremium.columnWeights = new double[] { 1.0, 0.0, 1.0, 1.0, 0.0, 1.0, Double.MIN_VALUE };
		gbl_panelCentralPremium.rowWeights = new double[] { 1.0, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0, Double.MIN_VALUE };
		panelCentralPremium.setLayout(gbl_panelCentralPremium);

		JComboBox comboBox = new JComboBox();
		comboBox.setEditable(true);
		comboBox.setSelectedItem("Escoge el descuento que deseas aplicar");

		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.gridwidth = 3;
		gbc_comboBox.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 1;
		gbc_comboBox.gridy = 3;
		panelCentralPremium.add(comboBox, gbc_comboBox);

		List<Descuento> descuentos = Controlador.getUnicaInstancia().getDescuentosAplicablesUsuario();
		// añadimos los descuentos aplicables al usuario al combobox
		for (Descuento descuento : descuentos) {
			comboBox.addItem(descuento.getNombre());
		}

		JButton btnNewButton_3 = new JButton("Pagar");
		btnNewButton_3.setPreferredSize(new Dimension(202, 70));
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// si es premium lo notifica, si no pues comprará el servicio con o sin
				// descuento depende de su seleccon del comboBox
				if (Controlador.getUnicaInstancia().isUsuarioPremium()) {
					JOptionPane.showMessageDialog(btnNewButton_3, "Usted ya es usuario premium", "Oops!",
							JOptionPane.INFORMATION_MESSAGE, null);
				} else if (descuentoEscogido) {
					JOptionPane.showMessageDialog(btnNewButton_3,
							"Se ha convertido en premium con un descuento del "
									+ descuentos.get(comboBox.getSelectedIndex()).calcDescuento() + "%",
							"Bienvenido", JOptionPane.INFORMATION_MESSAGE, null);
					Controlador.getUnicaInstancia().setUserPremium();
				} else {
					JOptionPane.showMessageDialog(btnNewButton_3,
							"Se ha convertido en premium, aunque sin uso de ningún descuento", "Bienvenido",
							JOptionPane.INFORMATION_MESSAGE, null);
					Controlador.getUnicaInstancia().setUserPremium();
				}
			}
		});

		JButton btnNewButton_1 = new JButton("Aplicar descuento");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Se escoge el descuento.
				String descuentoaux = (String) comboBox.getSelectedItem();
				if (!descuentoaux.equals("Escoge el descuento que deseas aplicar")) {
					JOptionPane.showMessageDialog(btnNewButton_1, "Enhorabuena, has aplicado un descuento",
							"Descuento aplicado", JOptionPane.INFORMATION_MESSAGE, null);
					descuentoEscogido = true;

				} else {
					JOptionPane.showMessageDialog(btnNewButton_1, "No has seleccionado ningun descuento",
							"Escoja descuento!", JOptionPane.INFORMATION_MESSAGE, null);

				}
			}
		});
		GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
		gbc_btnNewButton_1.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton_1.gridx = 4;
		gbc_btnNewButton_1.gridy = 3;
		panelCentralPremium.add(btnNewButton_1, gbc_btnNewButton_1);
		GridBagConstraints gbc_btnNewButton_3 = new GridBagConstraints();
		gbc_btnNewButton_3.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton_3.gridx = 1;
		gbc_btnNewButton_3.gridy = 4;
		panelCentralPremium.add(btnNewButton_3, gbc_btnNewButton_3);

		return panelCentralPremium;

	}

	private JPanel crearCentroMasRepro() throws DAOException {
		JPanel panelCentralMasRepro = new JPanel();
		panelCentralMasRepro.setBackground(new Color(173, 216, 230));
		GridBagLayout gbl_panelCentralMisListas = new GridBagLayout();
		gbl_panelCentralMisListas.columnWidths = new int[] { 0, 0, 0, 0, 0, 0, 0 };
		gbl_panelCentralMisListas.rowHeights = new int[] { 0, 0, 0, 0, 0, 53, 0 };
		gbl_panelCentralMisListas.columnWeights = new double[] { 1.0, 0.0, 1.0, 1.0, 0.0, 1.0, Double.MIN_VALUE };
		gbl_panelCentralMisListas.rowWeights = new double[] { 1.0, 0.0, 0.0, 1.0, 1.0, 1.0, Double.MIN_VALUE };
		panelCentralMasRepro.setLayout(gbl_panelCentralMisListas);

		JPanel panelTabla = new JPanel();
		panelTabla.setBorder(UIManager.getBorder("Menu.border"));
		panelTabla.setBackground(new Color(173, 216, 230));
		GridBagConstraints gbc_panelTabla = new GridBagConstraints();
		gbc_panelTabla.gridheight = 2;
		gbc_panelTabla.insets = new Insets(0, 0, 5, 5);
		gbc_panelTabla.gridwidth = 3;
		gbc_panelTabla.fill = GridBagConstraints.BOTH;
		gbc_panelTabla.gridx = 2;
		gbc_panelTabla.gridy = 1;
		panelTabla.setVisible(Controlador.getUnicaInstancia().isUsuarioPremium());
		panelCentralMasRepro.add(panelTabla, gbc_panelTabla);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setPreferredSize(new Dimension(200, 120));
		panelTabla.add(scrollPane_1);
		// tabla 6 es la tabla con las canciones más reproducidas en la app
		table_6 = new JTable();
		scrollPane_1.setViewportView(table_6);
		table_6.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table_6.setModel(
				new DefaultTableModel(new Object[][] {}, new String[] { "Titulo", "Interprete", "Nº reproducciones" }) {
					Class[] columnTypes = new Class[] { String.class, String.class, String.class };

					public Class getColumnClass(int columnIndex) {
						return columnTypes[columnIndex];
					}

					boolean[] columnEditables = new boolean[] { false, false, false };

					public boolean isCellEditable(int row, int column) {
						return columnEditables[column];
					}
				});

		DefaultTableModel model = (DefaultTableModel) table_6.getModel();
		// actualizamos el contenido de dicha tabla
		refrescarMasReproducidasTotal(model);

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 2;
		panelCentralMasRepro.add(scrollPane, gbc_scrollPane);
		scrollPane.setPreferredSize(new Dimension(200, 120));
		// tabla 5 es la tabla con las mas reproducidas por parte del usuario
		// correspondiente
		JTable table_5 = new JTable();
		scrollPane.setViewportView(table_5);
		table_5.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table_5.setModel(
				new DefaultTableModel(new Object[][] {}, new String[] { "Titulo", "Interprete", "Nº reproducciones" }) {
					Class[] columnTypes = new Class[] { String.class, String.class, String.class };

					public Class getColumnClass(int columnIndex) {
						return columnTypes[columnIndex];
					}

					boolean[] columnEditables = new boolean[] { false, false, false };

					public boolean isCellEditable(int row, int column) {
						return columnEditables[column];
					}
				});

		DefaultTableModel modell = (DefaultTableModel) table_5.getModel();
		// actualizamos el contenido de dicha tabla
		refrescarMasReproducidasUsuario(modell);
		JPanel panelBotones = new JPanel();
		panelBotones.setBorder(UIManager.getBorder("FileChooser.listViewBorder"));
		panelBotones.setBackground(new Color(173, 216, 230));
		GridBagConstraints gbc_panelBotones = new GridBagConstraints();
		gbc_panelBotones.insets = new Insets(0, 0, 5, 5);
		gbc_panelBotones.fill = GridBagConstraints.BOTH;
		gbc_panelBotones.gridx = 1;
		gbc_panelBotones.gridy = 3;
		panelCentralMasRepro.add(panelBotones, gbc_panelBotones);

		JButton play = new JButton("");
		play.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// reproducimos cancion seleccionada
				reproducirCancion(table_5, play, masReproducidasUsuario);
			}
		});
		play.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/um/tds/iconos/output-onlinepngtools (1).png")));
		play.setPreferredSize(new Dimension(50, 50));
		panelBotones.add(play);

		JButton pause = new JButton("");
		pause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// pausamos la cancion que se esté reproduciendo
				Controlador.getUnicaInstancia().pause();
			}
		});
		pause.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/um/tds/iconos/pause1.png")));
		pause.setPreferredSize(new Dimension(50, 50));
		panelBotones.add(pause);

		JButton atras = new JButton("");
		atras.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				reproducirAnteriorCancion();
			}
		});
		atras.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/um/tds/iconos/atras.png")));
		atras.setPreferredSize(new Dimension(50, 50));
		panelBotones.add(atras);

		JButton alante = new JButton("");
		alante.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				reproducirSiguienteCancion();
			}
		});
		alante.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/um/tds/iconos/siguiente.png")));
		alante.setPreferredSize(new Dimension(50, 50));
		panelBotones.add(alante);

		JPanel panelBotones_1 = new JPanel();
		panelBotones_1.setBorder(UIManager.getBorder("FileChooser.listViewBorder"));
		panelBotones_1.setBackground(new Color(173, 216, 230));
		GridBagConstraints gbc_panelBotones_1 = new GridBagConstraints();
		gbc_panelBotones_1.insets = new Insets(0, 0, 5, 5);
		gbc_panelBotones_1.fill = GridBagConstraints.BOTH;
		gbc_panelBotones_1.gridx = 2;
		gbc_panelBotones_1.gridy = 3;
		panelBotones_1.setVisible(Controlador.getUnicaInstancia().isUsuarioPremium());
		panelCentralMasRepro.add(panelBotones_1, gbc_panelBotones_1);

		JButton play_1 = new JButton("");
		play_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				reproducirCancion(table_6, play, masReproducidasSistema);
			}
		});
		play_1.setIcon(
				new ImageIcon(VentanaPrincipal.class.getResource("/um/tds/iconos/output-onlinepngtools (1).png")));
		play_1.setPreferredSize(new Dimension(50, 50));
		panelBotones_1.add(play_1);

		JButton pause_1 = new JButton("");
		pause_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Controlador.getUnicaInstancia().pause();
			}
		});
		pause_1.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/um/tds/iconos/pause1.png")));
		pause_1.setPreferredSize(new Dimension(50, 50));
		panelBotones_1.add(pause_1);

		JButton atras_1 = new JButton("");
		atras_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				reproducirAnteriorCancion();
			}
		});
		atras_1.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/um/tds/iconos/atras.png")));
		atras_1.setPreferredSize(new Dimension(50, 50));
		panelBotones_1.add(atras_1);

		JButton alante_1 = new JButton("");
		alante_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				reproducirSiguienteCancion();
			}
		});
		alante_1.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/um/tds/iconos/siguiente.png")));
		alante_1.setPreferredSize(new Dimension(50, 50));
		panelBotones_1.add(alante_1);
		JButton btnNewButton_2 = new JButton("<html>Refrescar mis canciones<br>más reproducidas</html>");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Actualizamos el contenido de las mas reproducidas por el usuario
				refrescarMasReproducidasUsuario(modell);
			}
		});

		GridBagConstraints gbc_btnNewButton_4 = new GridBagConstraints();
		gbc_btnNewButton_4.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton_4.gridx = 2;
		gbc_btnNewButton_4.gridy = 4;

		GridBagConstraints gbc_btnNewButton_2 = new GridBagConstraints();
		gbc_btnNewButton_2.insets = new Insets(0, 0, 0, 5);
		gbc_btnNewButton_2.gridx = 1;
		gbc_btnNewButton_2.gridy = 5;
		panelCentralMasRepro.add(btnNewButton_2, gbc_btnNewButton_2);

		JButton btnNewButton_2_1 = new JButton("<html>Refrescar las canciones<br>más reproducidas en la app</html>");
		btnNewButton_2_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					refrescarMasReproducidasTotal(model);
				} catch (DAOException e) {
					e.printStackTrace();
				}
			}
		});
		GridBagConstraints gbc_btnNewButton_2_1 = new GridBagConstraints();
		gbc_btnNewButton_2_1.insets = new Insets(0, 0, 0, 5);
		gbc_btnNewButton_2_1.gridx = 2;
		gbc_btnNewButton_2_1.gridy = 5;
		btnNewButton_2_1.setVisible(Controlador.getUnicaInstancia().isUsuarioPremium());
		panelCentralMasRepro.add(btnNewButton_2_1, gbc_btnNewButton_2_1);

		JButton btnNewButton_4 = new JButton("<html>Ver las canciones<br>más reproducidas de la app</html>");
		btnNewButton_4.setVisible(!Controlador.getUnicaInstancia().isUsuarioPremium());
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// si el usuario es disponible podrá ver las canciones mas reproducidas de la
				// app
				if (Controlador.getUnicaInstancia().isUsuarioPremium()) {
					panelBotones_1.setVisible(true);
					btnNewButton_2_1.setVisible(true);
					panelTabla.setVisible(true);
					btnNewButton_4.setVisible(false);
				} else
					JOptionPane.showMessageDialog(play, "Debes ser usuario premium", "Premium requerido",
							JOptionPane.INFORMATION_MESSAGE, null);
			}
		});
		panelCentralMasRepro.add(btnNewButton_4, gbc_btnNewButton_4);
		return panelCentralMasRepro;
	}

	public String rutaPDF = null;

	private JPanel crearCentroMisListas() {
		JPanel panelCentralMisListas = new JPanel();
		panelCentralMisListas.setBackground(new Color(173, 216, 230));
		GridBagLayout gbl_panelCentralMisListas = new GridBagLayout();
		gbl_panelCentralMisListas.columnWidths = new int[] { 0, 0, 0, 0, 0, 0 };
		gbl_panelCentralMisListas.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 53, 0 };
		gbl_panelCentralMisListas.columnWeights = new double[] { 1.0, 0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE };
		gbl_panelCentralMisListas.rowWeights = new double[] { 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE };
		panelCentralMisListas.setLayout(gbl_panelCentralMisListas);

		JPanel panelTabla = new JPanel();
		panelTabla.setBorder(UIManager.getBorder("Menu.border"));
		panelTabla.setBackground(new Color(173, 216, 230));
		GridBagConstraints gbc_panelTabla = new GridBagConstraints();
		gbc_panelTabla.gridheight = 2;
		gbc_panelTabla.insets = new Insets(0, 0, 5, 5);
		gbc_panelTabla.gridwidth = 3;
		gbc_panelTabla.fill = GridBagConstraints.BOTH;
		gbc_panelTabla.gridx = 1;
		gbc_panelTabla.gridy = 1;
		panelCentralMisListas.add(panelTabla, gbc_panelTabla);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(new Dimension(250, 200));
		panelTabla.add(scrollPane);
		// tabla con las canciones de cierta playlist seleccionada
		table_4 = new JTable();
		scrollPane.setViewportView(table_4);
		table_4.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table_4.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "Titulo", "Interprete" }) {
			Class[] columnTypes = new Class[] { String.class, String.class };

			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}

			boolean[] columnEditables = new boolean[] { false, false };

			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		JButton btnNewButton_6 = new JButton("Elegir directorio donde generar el pdf");
		btnNewButton_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// seleccion de directorio donde crear el pdf
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int option = fileChooser.showOpenDialog(frmVentanaPrincipal);
				if (option == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					rutaPDF = file.getAbsolutePath();
				}
			}
		});

		JButton btnNewButton_5 = new JButton("Generar pdf");
		btnNewButton_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// primero se ha de haber seleccionado el directorio, luego se comprueba si es
				// premium
				if (rutaPDF == null) {
					JOptionPane.showMessageDialog(btnNewButton_5,
							"Selecciona primero el directorio donde quieres que se genere", "Selecciona directorio",
							JOptionPane.ERROR_MESSAGE, null);
				} else {
					if (Controlador.getUnicaInstancia().isUsuarioPremium()) {
						try {
							Controlador.getUnicaInstancia().generarPdf(rutaPDF);
						} catch (FileNotFoundException | DocumentException e) {
							e.printStackTrace();
						}
						JOptionPane.showMessageDialog(btnNewButton_5, "Se ha generado el pdf correctamente",
								"PDF generado", JOptionPane.INFORMATION_MESSAGE, null);
					} else {
						JOptionPane.showMessageDialog(btnNewButton_5, "Debes ser premium para tener esta funcionalidad",
								"Debes ser premium", JOptionPane.INFORMATION_MESSAGE, null);
					}
				}
			}

		});

		JPanel panelBotones = new JPanel();
		panelBotones.setBorder(UIManager.getBorder("FileChooser.listViewBorder"));
		panelBotones.setBackground(new Color(173, 216, 230));
		GridBagConstraints gbc_panelBotones = new GridBagConstraints();
		gbc_panelBotones.insets = new Insets(0, 0, 5, 5);
		gbc_panelBotones.fill = GridBagConstraints.BOTH;
		gbc_panelBotones.gridx = 2;
		gbc_panelBotones.gridy = 4;
		panelCentralMisListas.add(panelBotones, gbc_panelBotones);

		JButton play = new JButton("");
		play.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// reproducir la cancion seleccionada de la playlist
				reproducirCancion(table_4, play, cancionesListaConcreta);
			}
		});
		play.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/um/tds/iconos/output-onlinepngtools (1).png")));
		play.setPreferredSize(new Dimension(50, 50));
		panelBotones.add(play);

		JButton pause = new JButton("");
		pause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Controlador.getUnicaInstancia().pause();
			}
		});
		pause.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/um/tds/iconos/pause1.png")));
		pause.setPreferredSize(new Dimension(50, 50));
		panelBotones.add(pause);

		JButton atras = new JButton("");
		atras.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				reproducirAnteriorCancion();
			}
		});
		atras.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/um/tds/iconos/atras.png")));
		atras.setPreferredSize(new Dimension(50, 50));
		panelBotones.add(atras);

		JButton alante = new JButton("");
		alante.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				reproducirSiguienteCancion();
			}
		});
		alante.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/um/tds/iconos/siguiente.png")));
		alante.setPreferredSize(new Dimension(50, 50));
		panelBotones.add(alante);

		GridBagConstraints gbc_btnNewButton_6 = new GridBagConstraints();
		gbc_btnNewButton_6.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton_6.gridx = 2;
		gbc_btnNewButton_6.gridy = 5;
		panelCentralMisListas.add(btnNewButton_6, gbc_btnNewButton_6);
		GridBagConstraints gbc_btnNewButton_5 = new GridBagConstraints();
		gbc_btnNewButton_5.insets = new Insets(0, 0, 0, 5);
		gbc_btnNewButton_5.gridx = 2;
		gbc_btnNewButton_5.gridy = 6;
		panelCentralMisListas.add(btnNewButton_5, gbc_btnNewButton_5);

		return panelCentralMisListas;
	}

	private JPanel crearCentroExplorar() {
		JPanel panelCentralExplorar = new JPanel();
		panelCentralExplorar.setBackground(new Color(173, 216, 230));
		GridBagLayout gbl_panelCentralExplorar = new GridBagLayout();
		gbl_panelCentralExplorar.columnWidths = new int[] { 0, 0, 0, 0, 0, 45, 0, 0, 0, 0, 0, 0 };
		gbl_panelCentralExplorar.rowHeights = new int[] { 0, 49, 0, 0, 30, 0, 0, 0 };
		gbl_panelCentralExplorar.columnWeights = new double[] { 1.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0, 1.0,
				Double.MIN_VALUE };
		gbl_panelCentralExplorar.rowWeights = new double[] { 1.0, 0.0, 0.0, 1.0, 1.0, 0.0, 1.0, Double.MIN_VALUE };
		panelCentralExplorar.setLayout(gbl_panelCentralExplorar);

		JButton btnNewButton = new JButton("Directorio con musica");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int option = fileChooser.showOpenDialog(frmVentanaPrincipal);
				if (option == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					ruta_fichero = file.getAbsolutePath();
					Controlador.getUnicaInstancia().cargarMusica(ruta_fichero);
				}
			}
		});
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton.gridx = 3;
		gbc_btnNewButton.gridy = 0;
		panelCentralExplorar.add(btnNewButton, gbc_btnNewButton);

		Luz luz = new Luz();
		luz.addEncendidoListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent arg0) {

				JFileChooser chooser = new JFileChooser();
				chooser.showSaveDialog(null);

				if (chooser.getSelectedFile() != null) {
					String fichero = chooser.getSelectedFile().getAbsolutePath();
					try {
						Controlador.getUnicaInstancia().cargarCanciones(fichero);
					} catch (DAOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		GridBagConstraints gbc_luz = new GridBagConstraints();
		gbc_luz.insets = new Insets(0, 0, 5, 5);
		gbc_luz.gridx = 6;
		gbc_luz.gridy = 0;
		panelCentralExplorar.add(luz, gbc_luz);

		JLabel lblNewLabel = new JLabel("Cargar canciones xml");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 7;
		gbc_lblNewLabel.gridy = 0;
		panelCentralExplorar.add(lblNewLabel, gbc_lblNewLabel);

		JPanel panelSuperior = new JPanel();
		panelSuperior.setBackground(new Color(173, 216, 230));
		panelSuperior.setBorder(UIManager.getBorder("FileChooser.listViewBorder"));
		GridBagConstraints gbc_panelSuperior = new GridBagConstraints();
		gbc_panelSuperior.gridwidth = 9;
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

		JComboBox comboBox = new JComboBox();
		comboBox.setEditable(true);
		comboBox.setSelectedItem("Estilo");
		comboBox.addItem("BOLERO");
		comboBox.addItem("ROMANTICA");
		comboBox.addItem("CANTAUTOR");
		comboBox.addItem("CLASICA");
		comboBox.addItem("FLAMENCO");
		comboBox.addItem("JAZZ");
		comboBox.addItem("OPERA");
		comboBox.addItem("POP");
		comboBox.addItem("ROCK");
		comboBox.addItem("ROCK-SINFONICO");
		comboBox.addItem("FOLK");
		comboBox.addItem("CABARET");
		comboBox.addItem("BALLAD");
		comboBox.addItem("TANGO");
		panelSuperior.add(comboBox);

		JPanel panelCentro = new JPanel();
		panelCentro.setBorder(UIManager.getBorder("FormattedTextField.border"));
		panelCentro.setBackground(new Color(173, 216, 230));
		GridBagConstraints gbc_panelCentro = new GridBagConstraints();
		gbc_panelCentro.insets = new Insets(0, 0, 5, 5);
		gbc_panelCentro.fill = GridBagConstraints.BOTH;
		gbc_panelCentro.gridx = 5;
		gbc_panelCentro.gridy = 2;
		panelCentralExplorar.add(panelCentro, gbc_panelCentro);

		JPanel panelTabla = new JPanel();
		panelTabla.setBackground(new Color(173, 216, 230));
		panelTabla.setBorder(UIManager.getBorder("Menu.border"));
		GridBagConstraints gbc_panelTabla = new GridBagConstraints();
		gbc_panelTabla.insets = new Insets(0, 0, 5, 5);
		gbc_panelTabla.fill = GridBagConstraints.BOTH;
		gbc_panelTabla.gridx = 5;
		gbc_panelTabla.gridy = 3;
		panelTabla.setVisible(false);
		panelCentralExplorar.add(panelTabla, gbc_panelTabla);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(new Dimension(350, 200));
		panelTabla.add(scrollPane);

		JButton btnBuscar_1 = new JButton("Buscar");
		btnBuscar_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// cuando se le da a buscar se ponen visibles los campos para interactuar con
				// las canciones, y se actualiza la tabla con las canciones devueltas por el
				// filtrarCanciones
				panelTabla.setVisible(true);
				ArrayList<Cancion> canciones = (ArrayList<Cancion>) Controlador.getUnicaInstancia().filtrarCanciones(
						txtInterprete_1.getText(), txtTitulo_1.getText(), (String) comboBox.getSelectedItem());
				cancionesExploradas = canciones;
				DefaultTableModel model = (DefaultTableModel) table_3.getModel();
				model.setRowCount(0);
				for (Cancion cancion : canciones) {
					((DefaultTableModel) table_3.getModel())
							.addRow(new Object[] { cancion.getTitulo(), cancion.getInterprete() });

				}
			}
		});
		panelCentro.add(btnBuscar_1);

		JButton btnCancelar_1 = new JButton("Cancelar");
		btnCancelar_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DefaultTableModel model = (DefaultTableModel) table_3.getModel();
				model.setRowCount(0);
				panelTabla.setVisible(false);
				txtInterprete_1.setText("Interprete");
				txtTitulo_1.setText("Titulo");
				comboBox.setSelectedItem("Estilo");

			}
		});
		panelCentro.add(btnCancelar_1);

		table_3 = new JTable();
		table_3.setPreferredSize(new Dimension(350, 200));
		table_3.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table_3.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "Titulo", "Interprete" }) {
			Class[] columnTypes = new Class[] { String.class, String.class };

			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}

			boolean[] columnEditables = new boolean[] { false, false };

			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		scrollPane.setViewportView(table_3);

		JPanel panelBotones = new JPanel();
		panelBotones.setBorder(UIManager.getBorder("FileChooser.listViewBorder"));
		panelBotones.setBackground(new Color(173, 216, 230));
		GridBagConstraints gbc_panelBotones = new GridBagConstraints();
		gbc_panelBotones.insets = new Insets(0, 0, 5, 5);
		gbc_panelBotones.gridwidth = 7;
		gbc_panelBotones.fill = GridBagConstraints.VERTICAL;
		gbc_panelBotones.gridx = 2;
		gbc_panelBotones.gridy = 5;
		panelCentralExplorar.add(panelBotones, gbc_panelBotones);

		JButton play = new JButton("");
		play.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				reproducirCancion(table_3, play, cancionesExploradas);
			}
		});
		play.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/um/tds/iconos/output-onlinepngtools (1).png")));
		play.setSelectedIcon(
				new ImageIcon(VentanaPrincipal.class.getResource("/um/tds/iconos/output-onlinepngtools (1).png")));
		play.setPreferredSize(new Dimension(50, 50));
		play.setMinimumSize(new Dimension(50, 50));
		play.setMaximumSize(new Dimension(50, 50));
		panelBotones.add(play);

		JButton pause = new JButton("");
		pause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Controlador.getUnicaInstancia().pause();
			}
		});
		pause.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/um/tds/iconos/pause1.png")));
		pause.setPreferredSize(new Dimension(50, 50));
		panelBotones.add(pause);

		JButton atras = new JButton("");
		atras.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				reproducirAnteriorCancion();
			}
		});
		atras.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/um/tds/iconos/atras.png")));
		atras.setPreferredSize(new Dimension(50, 50));
		panelBotones.add(atras);

		JButton alante = new JButton("");
		alante.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				reproducirSiguienteCancion();
			}
		});
		alante.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/um/tds/iconos/siguiente.png")));
		alante.setPreferredSize(new Dimension(50, 50));
		panelBotones.add(alante);

		return panelCentralExplorar;
	}

	private void reproducirCancion(JTable tabla, JButton play, ArrayList<Cancion> canciones) {
		// comprobamos si hay una canción pausada.
		boolean cancionPausada = Controlador.getUnicaInstancia().isCancionPausada();
		int cancionSeleccionada = tabla.getSelectedRow();
		tabla.clearSelection();
		// si hay una pausada y no se ha seleccionado canción, el botón play se habrá
		// presionado para que se continúe con la reproducción!
		if (cancionPausada && cancionSeleccionada == -1) {
			Controlador.getUnicaInstancia().play(null, null, 0);
			return;
		}
		// si no habia seleccionada y tampoco ninguna pausada, avisamos de que se debe
		// seleccionar una cancion, en su defecto se obtine la cancion seleccionada, se
		// reproduce con play(), y se actualiza la tabla de recientes
		if (cancionSeleccionada != -1) {
			Cancion cancion = canciones.get(cancionSeleccionada);
			Controlador.getUnicaInstancia().play(cancion, canciones, cancionSeleccionada);
			anadirCancionesRecientes();

		} else {
			JOptionPane.showMessageDialog(play, "No se ha seleccionado ninguna canción", "Debes elegir canción",
					JOptionPane.INFORMATION_MESSAGE, null);
		}
	}

	private void reproducirSiguienteCancion() {
		// llama a playNext() y actualiza la tabla de recientes con la canción que se
		// haya reproducido
		Cancion cancion = Controlador.getUnicaInstancia().playNext();
		if (cancion != null) {
			anadirCancionesRecientes();
		}
	}

	private void reproducirAnteriorCancion() {
		// lo mismo solo que con la cancion anterior
		Cancion cancion = Controlador.getUnicaInstancia().playPrevious();
		if (cancion != null) {
			anadirCancionesRecientes();
		}
	}

	private void anadirCancionesRecientes() {
		// obtenemos las recientes que tenga el controlador guardadas(ya incluida la
		// ultima reproducida como veremos en el controlador) y actualizamos la tabla
		this.cancionesRecientes = Controlador.getUnicaInstancia().getRecientes();
		DefaultTableModel model = (DefaultTableModel) tableRecientes.getModel();
		refrescarRecientes(model);
	}

	private JPanel crearCentroNuevaLista() {
		JPanel panelCentroNuevaLista = new JPanel();
		panelCentroNuevaLista.setBackground(new Color(173, 216, 230));
		GridBagLayout gbl_panelCentroNuevaLista = new GridBagLayout();
		gbl_panelCentroNuevaLista.columnWidths = new int[] { 50, 0, 0, 0, 89, 0 };
		gbl_panelCentroNuevaLista.rowHeights = new int[] { 39, 0, 0, 0, 0 };
		gbl_panelCentroNuevaLista.columnWeights = new double[] { 1.0, 1.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		gbl_panelCentroNuevaLista.rowWeights = new double[] { 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		panelCentroNuevaLista.setLayout(gbl_panelCentroNuevaLista);

		JPanel panelSuperiorIzq = new JPanel();
		panelSuperiorIzq.setBackground(new Color(173, 216, 230));
		panelSuperiorIzq.setBorder(UIManager.getBorder("FileChooser.listViewBorder"));
		GridBagConstraints gbc_panelSuperiorIzq = new GridBagConstraints();
		gbc_panelSuperiorIzq.insets = new Insets(0, 0, 5, 5);
		gbc_panelSuperiorIzq.fill = GridBagConstraints.BOTH;
		gbc_panelSuperiorIzq.gridx = 0;
		gbc_panelSuperiorIzq.gridy = 0;
		panelCentroNuevaLista.add(panelSuperiorIzq, gbc_panelSuperiorIzq);

		txtNombreLista = new JTextField();
		txtNombreLista.setText("Nombre lista");
		panelSuperiorIzq.add(txtNombreLista);
		txtNombreLista.setColumns(10);

		JPanel panelSuperiorDer = new JPanel();
		panelSuperiorDer.setBackground(new Color(173, 216, 230));
		panelSuperiorDer.setBorder(UIManager.getBorder("FileChooser.listViewBorder"));
		GridBagConstraints gbc_panelSuperiorDer = new GridBagConstraints();
		gbc_panelSuperiorDer.insets = new Insets(0, 0, 5, 0);
		gbc_panelSuperiorDer.fill = GridBagConstraints.BOTH;
		gbc_panelSuperiorDer.gridx = 4;
		gbc_panelSuperiorDer.gridy = 0;
		panelCentroNuevaLista.add(panelSuperiorDer, gbc_panelSuperiorDer);

		JPanel panelCrearLista = new JPanel();
		panelCrearLista.setVisible(false);
		panelCrearLista.setBackground(new Color(173, 216, 230));
		panelCrearLista.setBorder(UIManager.getBorder("FileChooser.listViewBorder"));

		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridwidth = 5;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 1;
		panelCentroNuevaLista.add(panelCrearLista, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 50, 60, 89, 0 };
		gbl_panel.rowHeights = new int[] { 0, 59, 59, 37, 0 };
		gbl_panel.columnWeights = new double[] { 1.0, 1.0, 1.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		panelCrearLista.setLayout(gbl_panel);

		JPanel panelArribaIzq = new JPanel();
		GridBagConstraints gbc_panelArribaIzq = new GridBagConstraints();
		gbc_panelArribaIzq.fill = GridBagConstraints.BOTH;
		gbc_panelArribaIzq.insets = new Insets(0, 0, 5, 5);
		gbc_panelArribaIzq.gridx = 0;
		gbc_panelArribaIzq.gridy = 0;
		panelCrearLista.add(panelArribaIzq, gbc_panelArribaIzq);
		panelArribaIzq.setBackground(new Color(173, 216, 230));
		panelArribaIzq.setBorder(UIManager.getBorder("FileChooser.listViewBorder"));

		txtInterprete = new JTextField();
		txtInterprete.setText("Interprete");
		panelArribaIzq.add(txtInterprete);
		txtInterprete.setColumns(10);

		txtTitulo = new JTextField();
		txtTitulo.setText("Titulo");
		panelArribaIzq.add(txtTitulo);
		txtTitulo.setColumns(10);

		JPanel panelArribaDer = new JPanel();
		GridBagConstraints gbc_panelArribaDer = new GridBagConstraints();
		gbc_panelArribaDer.fill = GridBagConstraints.BOTH;
		gbc_panelArribaDer.insets = new Insets(0, 0, 5, 0);
		gbc_panelArribaDer.gridx = 2;
		gbc_panelArribaDer.gridy = 0;
		panelCrearLista.add(panelArribaDer, gbc_panelArribaDer);
		panelArribaDer.setBackground(new Color(173, 216, 230));
		panelArribaDer.setBorder(UIManager.getBorder("FileChooser.listViewBorder"));

		JComboBox comboBox = new JComboBox();
		comboBox.setEditable(true);
		comboBox.setSelectedItem("Estilo");
		comboBox.addItem("BOLERO");
		comboBox.addItem("ROMANTICA");
		comboBox.addItem("CANTAUTOR");
		comboBox.addItem("CLASICA");
		comboBox.addItem("FLAMENCO");
		comboBox.addItem("JAZZ");
		comboBox.addItem("OPERA");
		comboBox.addItem("POP");
		comboBox.addItem("ROCK");
		comboBox.addItem("CUALQUIERA");
		panelArribaDer.add(comboBox);

		JPanel panelTablaIzq = new JPanel();
		GridBagConstraints gbc_panelTablaIzq = new GridBagConstraints();
		gbc_panelTablaIzq.anchor = GridBagConstraints.SOUTH;
		gbc_panelTablaIzq.fill = GridBagConstraints.HORIZONTAL;
		gbc_panelTablaIzq.gridheight = 2;
		gbc_panelTablaIzq.insets = new Insets(0, 0, 5, 5);
		gbc_panelTablaIzq.gridx = 0;
		gbc_panelTablaIzq.gridy = 1;
		panelCrearLista.add(panelTablaIzq, gbc_panelTablaIzq);
		panelTablaIzq.setBorder(UIManager.getBorder("Menu.border"));
		panelTablaIzq.setBackground(new Color(173, 216, 230));

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(new Dimension(200, 150));
		panelTablaIzq.add(scrollPane);
		// tabla correspondiente a las canciones filtradas y aun no añadidas a la
		// playlist que se desea crear
		table_1 = new JTable();
		scrollPane.setViewportView(table_1);
		table_1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table_1.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "Titulo", "Interprete" }) {
			Class[] columnTypes = new Class[] { String.class, String.class };

			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}

			boolean[] columnEditables = new boolean[] { false, false };

			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		scrollPane.setViewportView(table_1);

		JButton buscar = new JButton("Buscar");
		buscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// en caso de que haya una nueva busqueda por filtrado, vaciamos la lista de
				// canciones aun no añadidas, recuperamos las canciones devueltas tras el
				// filtrado y actualizamos la lista y la tabla con estas canciones
				cancionesNoAnadidas.clear();
				ArrayList<Cancion> canciones = (ArrayList<Cancion>) Controlador.getUnicaInstancia().filtrarCanciones(
						txtInterprete.getText(), txtTitulo.getText(), (String) comboBox.getSelectedItem());
				DefaultTableModel model = (DefaultTableModel) table_1.getModel();
				model.setRowCount(0);
				for (Cancion cancion : canciones) {
					((DefaultTableModel) table_1.getModel())
							.addRow(new Object[] { cancion.getTitulo(), cancion.getInterprete() });
					cancionesNoAnadidas.add(cancion);
				}
			}
		});
		panelArribaDer.add(buscar);

		JPanel panelCentro1 = new JPanel();
		GridBagConstraints gbc_panelCentro1 = new GridBagConstraints();
		gbc_panelCentro1.anchor = GridBagConstraints.SOUTH;
		gbc_panelCentro1.insets = new Insets(0, 0, 5, 5);
		gbc_panelCentro1.gridx = 1;
		gbc_panelCentro1.gridy = 1;
		panelCrearLista.add(panelCentro1, gbc_panelCentro1);
		panelCentro1.setBackground(new Color(173, 216, 230));
		panelCentro1.setBorder(UIManager.getBorder("Menu.border"));

		JButton izquierda = new JButton("<<");
		izquierda.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// se obtiene y elimina la cancion seleccionada y se pasa a la tabla de la
				// izquierda, añadiendola a la lista correspondiente(cancionesNoAnadidas) y
				// actualizando las tablas.
				int row = table_2.getSelectedRow();
				if (row != -1) {
					Cancion cancion = cancionesAnadidas.remove(row);
					if (cancion != null) {
						String[] rowData = new String[table_2.getColumnCount()];
						for (int i = 0; i < table_2.getColumnCount(); i++) {
							rowData[i] = (String) table_2.getValueAt(row, i);
						}
						DefaultTableModel modell = (DefaultTableModel) table_2.getModel();
						modell.removeRow(row);
						DefaultTableModel model = (DefaultTableModel) table_1.getModel();
						model.addRow(new Object[] { rowData[0], rowData[1] });
						cancionesNoAnadidas.add(cancion);
					}
				}
			}
		});
		panelCentro1.add(izquierda);
		JPanel panelTablaDer = new JPanel();
		GridBagConstraints gbc_panelTablaDer = new GridBagConstraints();
		gbc_panelTablaDer.anchor = GridBagConstraints.SOUTH;
		gbc_panelTablaDer.fill = GridBagConstraints.HORIZONTAL;
		gbc_panelTablaDer.gridheight = 2;
		gbc_panelTablaDer.insets = new Insets(0, 0, 5, 0);
		gbc_panelTablaDer.gridx = 2;
		gbc_panelTablaDer.gridy = 1;
		panelCrearLista.add(panelTablaDer, gbc_panelTablaDer);
		panelTablaDer.setBorder(UIManager.getBorder("Menu.border"));
		panelTablaDer.setBackground(new Color(173, 216, 230));

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setPreferredSize(new Dimension(200, 150));
		panelTablaDer.add(scrollPane_1);

		table_2 = new JTable();
		scrollPane_1.setViewportView(table_2);
		table_2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table_2.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "Titulo", "Interprete" }) {
			Class[] columnTypes = new Class[] { String.class, String.class };

			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}

			boolean[] columnEditables = new boolean[] { false, false };

			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		scrollPane_1.setViewportView(table_2);
		JPanel panelCentro2 = new JPanel();
		GridBagConstraints gbc_panelCentro2 = new GridBagConstraints();
		gbc_panelCentro2.anchor = GridBagConstraints.NORTH;
		gbc_panelCentro2.insets = new Insets(0, 0, 5, 5);
		gbc_panelCentro2.gridx = 1;
		gbc_panelCentro2.gridy = 2;
		panelCrearLista.add(panelCentro2, gbc_panelCentro2);
		panelCentro2.setBackground(new Color(173, 216, 230));
		panelCentro2.setBorder(UIManager.getBorder("Menu.border"));
		panelCentro2.setForeground(new Color(0, 0, 0));

		JButton derecha = new JButton(">>");
		derecha.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// lo mismo que el botón "izquierda" anterior solo que pasandolas a la derecha y
				// haciendo un tratamiento extra por si la canción ya se encuentra en la
				// playlist
				int row = table_1.getSelectedRow();
				if (row != -1) {
					for (Cancion cancion : cancionesAnadidas) {
						if ((cancion.getTitulo()).equals((String) table_1.getValueAt(row, 0))) {
							JOptionPane.showMessageDialog(derecha, "Esa canción ya se encuentra en la playlist",
									"Canción repetida", JOptionPane.INFORMATION_MESSAGE, null);
							return;
						}
					}
					Cancion cancion = cancionesNoAnadidas.remove(row);
					if (cancion != null) {
						String[] rowData = new String[table_1.getColumnCount()];
						for (int i = 0; i < table_1.getColumnCount(); i++) {
							rowData[i] = (String) table_1.getValueAt(row, i);
						}
						DefaultTableModel modell = (DefaultTableModel) table_1.getModel();
						modell.removeRow(row);
						DefaultTableModel model = (DefaultTableModel) table_2.getModel();
						model.addRow(new Object[] { rowData[0], rowData[1] });
						cancionesAnadidas.add(cancion);
					}
				}
			}
		});
		panelCentro2.add(derecha);

		JPanel panel = new JPanel();
		panel.setVisible(false);
		GridBagConstraints gbc_panel1 = new GridBagConstraints();
		gbc_panel1.insets = new Insets(0, 0, 0, 5);
		gbc_panel1.fill = GridBagConstraints.BOTH;
		gbc_panel1.gridx = 1;
		gbc_panel1.gridy = 3;
		panelCentroNuevaLista.add(panel, gbc_panel1);
		panel.setBorder(UIManager.getBorder("FileChooser.listViewBorder"));
		panel.setBackground(new Color(173, 216, 230));

		JButton aceptar = new JButton("Aceptar");
		aceptar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// si no se han añadido canciones a la playlist se avisa, else, se comprueba si
				// la playlist era repetida(variable la cual veremos enseguida como se
				// actualiza) si es repetida se llama a modificarPlaylist, si no a
				// registrarPlaylist, y una vez creada o modificada se vacían las tablas y
				// listas con las canciones y se actualizan variables de forma que la ventana
				// quede preparada para la creación de otra playlist!
				if (!(cancionesAnadidas.isEmpty())) {

					panel.setVisible(false);
					if (playlistRepe) {
						Controlador.getUnicaInstancia().modificarPlaylist(cancionesAnadidas, txtNombreLista.getText());
						txtNombreLista.setEditable(false);
					} else
						Controlador.getUnicaInstancia().registrarPlaylist(cancionesAnadidas, txtNombreLista.getText());

					((DefaultTableModel) table_1.getModel()).setRowCount(0);
					((DefaultTableModel) table_2.getModel()).setRowCount(0);
					txtInterprete.setText("Interprete");
					txtTitulo.setText("Titulo");
					comboBox.setSelectedItem("Estilo");
					panelCrearLista.setVisible(false);
					panel.setVisible(false);
					txtNombreLista.setEditable(true);
					txtNombreLista.setText("Nombre lista");
					cancionesNoAnadidas.clear();
					cancionesAnadidas.clear();
					playlistRepe = false;
				} else {
					JOptionPane.showMessageDialog(aceptar, "No se puede crear una playlist sin canciones!", "Error",
							JOptionPane.ERROR_MESSAGE, null);

				}

			}
		});
		panel.add(aceptar);

		JButton cancelar = new JButton("Cancelar");
		cancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// si se cancela la creación, se actualiza la ventana para que quede preparada
				// para una nueva creación de playlst, importante: se vacían las listas con las
				// canciones anadidas o no anadidas!
				((DefaultTableModel) table_1.getModel()).setRowCount(0);
				((DefaultTableModel) table_2.getModel()).setRowCount(0);
				cancionesNoAnadidas.clear();
				cancionesAnadidas.clear();
				panel.setVisible(false);
				panelCrearLista.setVisible(false);
				txtNombreLista.setEditable(true);
				playlistRepe = false;
			}
		});
		panel.add(cancelar);
		panelCentroNuevaLista.add(panel, gbc_panel1);
		JButton crear = new JButton("Crear");
		crear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// una vez seleccionado el nombre y se presione el botón de "crear" se comprueba
				// si ya hay una lista con ese nombre con "checkNombreListaIgual" y si es así
				// pues se pone playlistRepe a true, y se actualiza la tabla_2 la cual es la de
				// las canciones añadidas a cierta playlist, con las canciones ya contenidas en
				// la lista repetida
				String nombreLista = txtNombreLista.getText();
				String[] options = new String[] { "Si", "No" };
				int option = JOptionPane.showOptionDialog(panel, "Deseas crear una nueva lista?", "Crear nueva lista?",
						JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
				if (option == 0) {
					if (Controlador.getUnicaInstancia().checkNombreListaIgual(nombreLista)) {
						JOptionPane.showMessageDialog(panel,
								"Ya existe una lista con dicho nombre, podrás editarla si así lo deseas",
								"Nombre existente", JOptionPane.INFORMATION_MESSAGE, null);
						txtNombreLista.setEditable(false);
						panelCrearLista.setVisible(true);
						panel.setVisible(true);
						playlistRepe = true;
						ListaCanciones playlistConcreta = Controlador.getUnicaInstancia()
								.getPlaylistConcretaUsuario(nombreLista);
						DefaultTableModel model = (DefaultTableModel) table_2.getModel();
						model.setRowCount(0);
						for (Cancion cancion : playlistConcreta.getCanciones()) {
							((DefaultTableModel) table_2.getModel())
									.addRow(new Object[] { cancion.getTitulo(), cancion.getInterprete() });
							cancionesAnadidas.add(cancion);
						}
					} else { // esta es en la que no es recientes ni está editando, el común
						panelCrearLista.setVisible(true);
						panel.setVisible(true);
						txtNombreLista.setEditable(false);
					}
				}
			}
		});

		panelSuperiorDer.add(crear);
		return panelCentroNuevaLista;
	}

	// he convertido la tabla de recientes en global ya que debido a como hizo el
	// swing mi compañero con división en métodos necesitaba poder referenciarla de
	// manera global, soy consciente de que es una manera no muy ortodoxa
	public JTable tableRecientes = new JTable();
	private JTable table_6;

	private JPanel crearCentroReciente() {
		JPanel panelReciente = new JPanel();
		panelReciente.setBackground(new Color(173, 216, 230));
		GridBagLayout gbl_panelReciente = new GridBagLayout();
		gbl_panelReciente.columnWidths = new int[] { 90, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_panelReciente.rowHeights = new int[] { 33, 0, 0 };
		gbl_panelReciente.columnWeights = new double[] { 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		gbl_panelReciente.rowWeights = new double[] { 1.0, 0.0, Double.MIN_VALUE };
		panelReciente.setLayout(gbl_panelReciente);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(new Dimension(350, 200));
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		panelReciente.add(scrollPane, gbc_scrollPane);
		tableRecientes.setPreferredSize(new Dimension(350, 200));

		tableRecientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableRecientes.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "Titulo", "Interprete" }) {
			Class[] columnTypes = new Class[] { String.class, String.class };

			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}

			boolean[] columnEditables = new boolean[] { false, false };

			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		scrollPane.setViewportView(tableRecientes);
		DefaultTableModel model = (DefaultTableModel) tableRecientes.getModel();
		// actualizamos la tabla de recientes nada más la creamos.
		refrescarRecientes(model);
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridwidth = 9;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 1;
		panelReciente.add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 90, 0, 100, 0 };
		gbl_panel.rowHeights = new int[] { 0, 0 };
		gbl_panel.columnWeights = new double[] { 1.0, 0.0, 1.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		panel.setLayout(gbl_panel);
		JPanel panelIz = new JPanel();
		GridBagConstraints gbc_panelIz = new GridBagConstraints();
		gbc_panelIz.anchor = GridBagConstraints.NORTH;
		gbc_panelIz.fill = GridBagConstraints.HORIZONTAL;
		gbc_panelIz.insets = new Insets(0, 0, 0, 5);
		gbc_panelIz.gridx = 0;
		gbc_panelIz.gridy = 0;
		panel.add(panelIz, gbc_panelIz);
		panelIz.setBorder(UIManager.getBorder("CheckBoxMenuItem.border"));
		panelIz.setBackground(new Color(173, 216, 230));
		panelIz.setLayout(new GridLayout(0, 2, 0, 0));

		JButton atras = new JButton("");
		atras.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				reproducirAnteriorCancion();
			}
		});
		atras.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/um/tds/iconos/atras.png")));
		atras.setPreferredSize(new Dimension(50, 50));
		atras.setMinimumSize(new Dimension(50, 50));
		atras.setMaximumSize(new Dimension(50, 50));
		atras.setBackground(new Color(173, 216, 230));
		panelIz.add(atras);

		JButton play = new JButton("");
		play.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/um/tds/iconos/output-onlinepngtools (1).png")));
		play.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				reproducirCancion(tableRecientes, play, cancionesRecientes);
			}
		});
		play.setPreferredSize(new Dimension(50, 50));
		play.setMinimumSize(new Dimension(50, 50));
		play.setMaximumSize(new Dimension(50, 50));
		play.setBackground(new Color(173, 216, 230));
		panelIz.add(play);

		JPanel panelDer = new JPanel();
		GridBagConstraints gbc_panelDer = new GridBagConstraints();
		gbc_panelDer.anchor = GridBagConstraints.NORTH;
		gbc_panelDer.fill = GridBagConstraints.HORIZONTAL;
		gbc_panelDer.gridx = 2;
		gbc_panelDer.gridy = 0;
		panel.add(panelDer, gbc_panelDer);
		panelDer.setBorder(UIManager.getBorder("CheckBoxMenuItem.border"));
		panelDer.setBackground(new Color(173, 216, 230));
		panelDer.setLayout(new GridLayout(0, 2, 0, 0));

		JButton pause = new JButton("");
		pause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Controlador.getUnicaInstancia().pause();
			}
		});
		pause.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/um/tds/iconos/pause1.png")));
		pause.setPreferredSize(new Dimension(50, 50));
		pause.setMinimumSize(new Dimension(50, 50));
		pause.setMaximumSize(new Dimension(50, 50));
		pause.setBackground(new Color(173, 216, 230));
		panelDer.add(pause);

		JButton alante = new JButton("");
		alante.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				reproducirSiguienteCancion();
			}
		});
		alante.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/um/tds/iconos/siguiente.png")));
		alante.setPreferredSize(new Dimension(50, 50));
		alante.setMinimumSize(new Dimension(50, 50));
		alante.setMaximumSize(new Dimension(50, 50));
		alante.setBackground(new Color(173, 216, 230));
		panelDer.add(alante);

		return panelReciente;

	}

	private void crearPanelPrincipal() {
		JPanel contentPane = (JPanel) frmVentanaPrincipal.getContentPane();
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
	}

	private void crearPanelSuperior() {
		frmVentanaPrincipal.getContentPane().setLayout(new BorderLayout(0, 0));
		JPanel parteSuperior = new JPanel();
		parteSuperior.setBackground(new Color(100, 149, 237));
		parteSuperior.setBorder(UIManager.getBorder("PopupMenu.border"));
		frmVentanaPrincipal.getContentPane().add(parteSuperior, BorderLayout.NORTH);
		parteSuperior.setLayout(new BoxLayout(parteSuperior, BoxLayout.X_AXIS));

		JPanel mensajeBienvenida = new JPanel();
		mensajeBienvenida.setBackground(new Color(173, 216, 230));
		mensajeBienvenida.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		mensajeBienvenida.setBorder(UIManager.getBorder("TextField.border"));
		parteSuperior.add(mensajeBienvenida);
		mensajeBienvenida.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JLabel lblBienvenido = new JLabel("Bienvenido " + Controlador.getUnicaInstancia().getNombreUsuario());
		lblBienvenido.setForeground(SystemColor.windowBorder);
		lblBienvenido.setFont(new Font("Microsoft JhengHei Light", Font.BOLD, 15));
		mensajeBienvenida.add(lblBienvenido);

		JPanel botonPremiumSalir = new JPanel();
		botonPremiumSalir.setBackground(new Color(173, 216, 230));
		botonPremiumSalir.setBorder(UIManager.getBorder("TextField.border"));
		parteSuperior.add(botonPremiumSalir);
		botonPremiumSalir.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JButton premium = new JButton("Premium");
		premium.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				CardLayout cardLayout = (CardLayout) panelCentral.getLayout();
				cardLayout.show(panelCentral, "premium");

				frmVentanaPrincipal.getContentPane().revalidate();
				frmVentanaPrincipal.getContentPane().repaint();
				frmVentanaPrincipal.validate();
			}
		});
		premium.setBackground(new Color(255, 255, 255));
		botonPremiumSalir.add(premium);

		JButton salir = new JButton("Salir");
		salir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		botonPremiumSalir.add(salir);
	}

	private void crearPanelIzquierdo() {
		JPanel panelLeft = new JPanel();
		panelLeft.setBackground(new Color(100, 149, 237));
		frmVentanaPrincipal.getContentPane().add(panelLeft, BorderLayout.WEST);
		GridBagLayout gbl_panelLeft = new GridBagLayout();
		gbl_panelLeft.columnWidths = new int[] { 130, 0 };
		gbl_panelLeft.rowHeights = new int[] { 0, 0, 0, 47, 52, 0, 0 };
		gbl_panelLeft.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_panelLeft.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		panelLeft.setLayout(gbl_panelLeft);

		// ScrollPane
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVisible(false);

		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 5;
		panelLeft.add(scrollPane, gbc_scrollPane);

		DefaultListModel<String> model = new DefaultListModel<String>();
		// este jlist es el contenedor para los nombres de las listas de canciones del
		// usuario
		JList list = new JList(model);
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				// cada vez que se seleccione una lista, se actualiza la tabla_4, que es la que
				// mostrará las canciones, llamando al controlador con getPlaylistsUsuario, y
				// actualizando la lista de las canciones de la lista seleccionado en concreto,
				// para posteriormente actualizar la tabla con dichas canciones
				DefaultTableModel model = (DefaultTableModel) table_4.getModel();
				model.setRowCount(0);
				String playlistName = (String) list.getSelectedValue();
				List<ListaCanciones> playlistsUser = Controlador.getUnicaInstancia().getPlaylistsUsuario();
				for (ListaCanciones listacanciones : playlistsUser) {
					if (listacanciones.getNombre().equals(playlistName)) {
						cancionesListaConcreta = (ArrayList<Cancion>) listacanciones.getCanciones();
						for (Cancion cancion : cancionesListaConcreta) {
							model.addRow(new Object[] { cancion.getTitulo(), cancion.getInterprete() });
						}
					}
				}
			}
		});
		list.setVisible(false);
		// se le pasa el modelo de la JList y se actualiza su contenido.
		refrescarPlaylists(model);
		scrollPane.setViewportView(list);

		// Explorar
		JPanel seccionExplorar = new JPanel();
		GridBagConstraints gbc_seccionExplorar = new GridBagConstraints();
		gbc_seccionExplorar.fill = GridBagConstraints.HORIZONTAL;
		gbc_seccionExplorar.anchor = GridBagConstraints.NORTH;
		gbc_seccionExplorar.insets = new Insets(0, 0, 5, 0);
		gbc_seccionExplorar.gridx = 0;
		gbc_seccionExplorar.gridy = 0;
		panelLeft.add(seccionExplorar, gbc_seccionExplorar);
		seccionExplorar.setBackground(new Color(173, 216, 230));
		seccionExplorar.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JLabel lblExplorar = new JLabel("    Explorar   ");
		lblExplorar.setHorizontalAlignment(SwingConstants.CENTER);
		lblExplorar.setFont(new Font("Tahoma", Font.BOLD, 12));
		seccionExplorar.add(lblExplorar);

		JButton lupa = new JButton("");
		lupa.setBorder(null);
		lupa.setBorderPainted(false);
		lupa.setContentAreaFilled(false);
		lupa.setOpaque(false);

		// ACCION CAMBIO PANEL EXPLORAR

		lupa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				scrollPane.setVisible(false);
				CardLayout cardLayout = (CardLayout) panelCentral.getLayout();
				cardLayout.show(panelCentral, "explorar");

				frmVentanaPrincipal.getContentPane().revalidate();
				frmVentanaPrincipal.getContentPane().repaint();
				frmVentanaPrincipal.validate();
			}

		});

		seccionExplorar.add(lupa);
		lupa.setPreferredSize(new Dimension(35, 35));
		lupa.setMinimumSize(new Dimension(14, 14));
		lupa.setMaximumSize(new Dimension(14, 14));
		lupa.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/um/tds/iconos/lupa1.png")));

		// MasReproducidas
		JPanel seccionMasRepro = new JPanel();
		seccionMasRepro.setBackground(new Color(173, 216, 230));
		GridBagConstraints gbc_seccionMasRepro = new GridBagConstraints();
		gbc_seccionMasRepro.insets = new Insets(0, 0, 5, 0);
		gbc_seccionMasRepro.fill = GridBagConstraints.BOTH;
		gbc_seccionMasRepro.gridx = 0;
		gbc_seccionMasRepro.gridy = 4;
		panelLeft.add(seccionMasRepro, gbc_seccionMasRepro);
		seccionMasRepro.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JLabel lblMasRepro = new JLabel("Mas Reproducidas");
		lblMasRepro.setHorizontalAlignment(SwingConstants.CENTER);
		lblMasRepro.setFont(new Font("Tahoma", Font.BOLD, 12));
		seccionMasRepro.add(lblMasRepro);

		JButton repro = new JButton("");
		repro.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/um/tds/iconos/masreproducidasss.png")));
		repro.setPreferredSize(new Dimension(35, 35));
		repro.setOpaque(false);
		repro.setMinimumSize(new Dimension(14, 14));
		repro.setMaximumSize(new Dimension(14, 14));
		repro.setContentAreaFilled(false);
		repro.setBorderPainted(false);
		repro.setBorder(null);
		seccionMasRepro.add(repro);

		// ACCION CAMBIO PANEL MAS REPRODUCIDAS

		repro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// este scrollPane es donde se encuentra la JList que contiene las playlist del
				// usuario cuando entras en la sección de "mis listas", se pone invisible cada
				// vez que se cambia de sección que no sea la de mis listas
				scrollPane.setVisible(false);
				CardLayout cardLayout = (CardLayout) panelCentral.getLayout();
				cardLayout.show(panelCentral, "masRepro");
				frmVentanaPrincipal.getContentPane().revalidate();
				frmVentanaPrincipal.getContentPane().repaint();
				frmVentanaPrincipal.validate();
			}
		});

		// NuevaLista
		JPanel seccionNuevaLista = new JPanel();
		GridBagConstraints gbc_seccionNuevaLista = new GridBagConstraints();
		gbc_seccionNuevaLista.fill = GridBagConstraints.HORIZONTAL;
		gbc_seccionNuevaLista.anchor = GridBagConstraints.NORTH;
		gbc_seccionNuevaLista.insets = new Insets(0, 0, 5, 0);
		gbc_seccionNuevaLista.gridx = 0;
		gbc_seccionNuevaLista.gridy = 1;
		panelLeft.add(seccionNuevaLista, gbc_seccionNuevaLista);
		seccionNuevaLista.setBackground(new Color(173, 216, 230));
		seccionNuevaLista.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JLabel lblNuevaLista = new JLabel("Nueva Lista ");
		lblNuevaLista.setFont(new Font("Tahoma", Font.BOLD, 12));
		seccionNuevaLista.add(lblNuevaLista);

		JButton nuevaLista = new JButton("");
		nuevaLista.setBorder(null);
		nuevaLista.setBorderPainted(false);
		nuevaLista.setContentAreaFilled(false);
		nuevaLista.setOpaque(false);

		// CAMBIO PANEL NUEVA LISTA

		nuevaLista.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// este scrollPane es donde se encuentra la JList que contiene las playlist del
				// usuario cuando entras en la sección de "mis listas", se pone invisible cada
				// vez que se cambia de sección que no sea la de mis listas
				scrollPane.setVisible(false);
				CardLayout cardLayout = (CardLayout) panelCentral.getLayout();
				cardLayout.show(panelCentral, "nuevaLista");

				frmVentanaPrincipal.getContentPane().revalidate();
				frmVentanaPrincipal.getContentPane().repaint();
				frmVentanaPrincipal.validate();
			}
		});

		seccionNuevaLista.add(nuevaLista);
		nuevaLista.setMinimumSize(new Dimension(35, 35));
		nuevaLista.setMaximumSize(new Dimension(35, 35));
		nuevaLista.setPreferredSize(new Dimension(35, 35));
		nuevaLista
				.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/um/tds/iconos/output-onlinepngtools.png")));

		// Reciente
		JPanel seccionReciente = new JPanel();
		GridBagConstraints gbc_seccionReciente = new GridBagConstraints();
		gbc_seccionReciente.fill = GridBagConstraints.HORIZONTAL;
		gbc_seccionReciente.anchor = GridBagConstraints.NORTH;
		gbc_seccionReciente.insets = new Insets(0, 0, 5, 0);
		gbc_seccionReciente.gridx = 0;
		gbc_seccionReciente.gridy = 2;
		panelLeft.add(seccionReciente, gbc_seccionReciente);
		seccionReciente.setBackground(new Color(173, 216, 230));

		JLabel lblReciente = new JLabel("  Reciente    ");
		lblReciente.setFont(new Font("Tahoma", Font.BOLD, 12));
		seccionReciente.add(lblReciente);

		JButton reciente = new JButton("");
		reciente.setBorder(null);
		reciente.setBorderPainted(false);
		reciente.setContentAreaFilled(false);
		reciente.setOpaque(false);
		reciente.setPreferredSize(new Dimension(35, 35));
		reciente.setMinimumSize(new Dimension(35, 35));
		reciente.setMaximumSize(new Dimension(35, 35));
		reciente.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/um/tds/iconos/recientes1.png")));

		// CAMBIO PANEL RECIENTE
		;

		reciente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// este scrollPane es donde se encuentra la JList que contiene las playlist del
				// usuario cuando entras en la sección de "mis listas", se pone invisible cada
				// vez que se cambia de sección que no sea la de mis listas
				scrollPane.setVisible(false);
				CardLayout cardLayout = (CardLayout) panelCentral.getLayout();
				cardLayout.show(panelCentral, "reciente");

				frmVentanaPrincipal.getContentPane().revalidate();
				frmVentanaPrincipal.getContentPane().repaint();
				frmVentanaPrincipal.validate();
			}
		});

		seccionReciente.add(reciente);

		// MisListas
		JPanel seccionMisListas = new JPanel();
		GridBagConstraints gbc_seccionMisListas = new GridBagConstraints();
		gbc_seccionMisListas.insets = new Insets(0, 0, 5, 0);
		gbc_seccionMisListas.fill = GridBagConstraints.HORIZONTAL;
		gbc_seccionMisListas.anchor = GridBagConstraints.NORTH;
		gbc_seccionMisListas.gridx = 0;
		gbc_seccionMisListas.gridy = 3;
		panelLeft.add(seccionMisListas, gbc_seccionMisListas);
		seccionMisListas.setBackground(new Color(173, 216, 230));

		JLabel lblMisListas = new JLabel("  Mis listas   ");
		lblMisListas.setFont(new Font("Tahoma", Font.BOLD, 12));
		seccionMisListas.add(lblMisListas);

		JButton listas = new JButton("");
		listas.setBorder(null);
		listas.setBorderPainted(false);
		listas.setContentAreaFilled(false);
		listas.setOpaque(false);
		listas.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/um/tds/iconos/lista1.png")));
		listas.setPreferredSize(new Dimension(35, 35));
		listas.setMinimumSize(new Dimension(35, 35));
		listas.setMaximumSize(new Dimension(35, 35));

		// CAMBIO PANEL MIS LISTAS

		listas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scrollPane.setVisible(true);
				list.setVisible(true);
				refrescarPlaylists(model);
				CardLayout cardLayout = (CardLayout) panelCentral.getLayout();
				cardLayout.show(panelCentral, "misListas");
				scrollPane.setVisible(true);
				frmVentanaPrincipal.getContentPane().revalidate();
				frmVentanaPrincipal.getContentPane().repaint();
				frmVentanaPrincipal.validate();
			}
		});

		seccionMisListas.add(listas);

	}

	private void refrescarPlaylists(DefaultListModel<String> model) {
		// simplemente se usa este método para actualizar la JList con los nombres de
		// las playlists del usuario
		model.clear();
		List<ListaCanciones> playlists_usuario = Controlador.getUnicaInstancia().getPlaylistsUsuario();

		for (ListaCanciones listaCanciones : playlists_usuario) {

			model.addElement(listaCanciones.getNombre());
		}
	}

	private void crearPanelCentral(JPanel panelR, JPanel panelE, JPanel panelM, JPanel panelN, JPanel panelMR,
			JPanel panelPremium) {
		panelCentral = new JPanel();
		panelCentral.setBackground(new Color(173, 216, 230));
		frmVentanaPrincipal.getContentPane().add(panelCentral, BorderLayout.CENTER);
		panelCentral.setLayout(new CardLayout(0, 0));
		// panelCentral.setSize(300,LastSongReproducida);
		panelCentral.add(panelN, "nuevaLista");
		panelCentral.add(panelR, "reciente");
		panelCentral.add(panelE, "explorar");
		panelCentral.add(panelM, "misListas");
		panelCentral.add(panelMR, "masRepro");
		panelCentral.add(panelPremium, "premium");
		CardLayout cardLayout = (CardLayout) panelCentral.getLayout();
		cardLayout.show(panelCentral, "reciente");
	}

	private void refrescarRecientes(DefaultTableModel model) {
		model.setRowCount(0);
		for (Cancion cancion : cancionesRecientes) {
			model.addRow(new Object[] { cancion.getTitulo(), cancion.getInterprete() });
		}
	}

	private void refrescarMasReproducidasUsuario(DefaultTableModel model) {
		// se obtienen las mas reproducidas por el usuario en cuestión y se añaden a la
		// lista correspondiente de canciones y a la tabla en la que se muestran
		model.setRowCount(0);
		HashMap<Cancion, Integer> canciones = new HashMap<Cancion, Integer>(
				Controlador.getUnicaInstancia().getMasReproducidasUsuario());
		for (Cancion cancion : canciones.keySet()) {
			masReproducidasUsuario.add(cancion);
			model.addRow(new Object[] { cancion.getTitulo(), cancion.getInterprete(), canciones.get(cancion) });
		}
	}

	private void refrescarMasReproducidasTotal(DefaultTableModel model) throws DAOException {
		// mas de lo mismo pero para las canciones mas reproducidas en toda la app
		model.setRowCount(0);
		for (Cancion cancion : Controlador.getUnicaInstancia().getMasReproducidasSistema()) {
			masReproducidasSistema.add(cancion);
			model.addRow(new Object[] { cancion.getTitulo(), cancion.getInterprete(), cancion.getNumReproducciones() });
		}
	}
}