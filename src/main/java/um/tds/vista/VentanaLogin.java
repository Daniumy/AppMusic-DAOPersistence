package um.tds.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import um.tds.controlador.Controlador;
import javax.swing.border.EmptyBorder;
import java.awt.Toolkit;
import javax.swing.UIManager;
import javax.swing.ImageIcon;
import java.awt.Dimension;
import java.awt.EventQueue;

public class VentanaLogin {

	private JFrame frmLogin;
	private JTextField textUsuario;
	private JPasswordField textPassword;

	/**
	 * Create the application.
	 */
	public VentanaLogin() {
		initialize();
	}

	public void mostrarVentana() {
		frmLogin.setLocationRelativeTo(null);
		frmLogin.setVisible(true);
	}
	
	private void initialize() {
		frmLogin = new JFrame();
		frmLogin.getContentPane().setForeground(Color.BLACK);
		frmLogin.getContentPane().setBackground(Color.WHITE);
		frmLogin.setIconImage(Toolkit.getDefaultToolkit().getImage(VentanaLogin.class.getResource("/um/tds/iconos/logo1.png")));
		frmLogin.setTitle("Login Music App");
		frmLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmLogin.getContentPane().setLayout(new BorderLayout());

		crearPanelTitulo();
		crearPanelLogin();

		frmLogin.setResizable(false);
		frmLogin.pack();
	}

	private void crearPanelTitulo() {
		JPanel panel_Norte = new JPanel();
		panel_Norte.setBorder(null);
		panel_Norte.setBackground(new Color(173, 216, 230));
		panel_Norte.setForeground(Color.BLACK);
		frmLogin.getContentPane().add(panel_Norte, BorderLayout.NORTH);
		panel_Norte.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 15));
		
		JButton button = new JButton("");
		button.setBorder(null);
		button.setBorderPainted(false);
		button.setContentAreaFilled(false);
		button.setOpaque(false);
		button.setPreferredSize(new Dimension(40, 40));
		button.setIcon(new ImageIcon(VentanaLogin.class.getResource("/um/tds/iconos/logo1.png")));
		panel_Norte.add(button);

		JLabel lblTitulo = new JLabel("Music App");
		lblTitulo.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblTitulo.setForeground(Color.DARK_GRAY);
		panel_Norte.add(lblTitulo);
	}

	private void crearPanelLogin() {
		JPanel panelLogin = new JPanel();
		panelLogin.setBackground(new Color(173, 216, 230));
		panelLogin.setBorder(new EmptyBorder(10, 10, 10, 10));
		frmLogin.getContentPane().add(panelLogin, BorderLayout.CENTER);
		panelLogin.setLayout(new BorderLayout(0, 0));

		panelLogin.add(crearPanelCampos(), BorderLayout.NORTH);
		panelLogin.add(crearPanelBotones(), BorderLayout.SOUTH);
	}

	private JPanel crearPanelCampos() {
		JPanel panelCampos = new JPanel();
		panelCampos.setBackground(new Color(100, 149, 237));
		panelCampos.setBorder(new TitledBorder(null, "Login", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelCampos.setLayout(new BoxLayout(panelCampos, BoxLayout.Y_AXIS));

		// Panel Campo Login
		JPanel panelCampoUsuario = new JPanel();
		panelCampoUsuario.setBorder(UIManager.getBorder("FileChooser.listViewBorder"));
		panelCampoUsuario.setBackground(new Color(173, 216, 230));
		panelCampos.add(panelCampoUsuario);
		panelCampoUsuario.setLayout(new BorderLayout(0, 0));

		JLabel lblUsuario = new JLabel("Usuario: ");
		panelCampoUsuario.add(lblUsuario);
		lblUsuario.setHorizontalAlignment(SwingConstants.RIGHT);
		lblUsuario.setFont(new Font("Tahoma", Font.PLAIN, 12));

		textUsuario = new JTextField();
		panelCampoUsuario.add(textUsuario, BorderLayout.EAST);
		textUsuario.setColumns(15);

		// Panel Campo Password
		JPanel panelCampoPassword = new JPanel();
		panelCampoPassword.setBorder(UIManager.getBorder("FileChooser.listViewBorder"));
		panelCampoPassword.setBackground(new Color(173, 216, 230));
		panelCampos.add(panelCampoPassword);
		panelCampoPassword.setLayout(new BorderLayout(0, 0));

		JLabel lblPassword = new JLabel("Contrase\u00F1a: ");
		panelCampoPassword.add(lblPassword);
		lblPassword.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 12));

		textPassword = new JPasswordField();
		panelCampoPassword.add(textPassword, BorderLayout.EAST);
		textPassword.setColumns(15);
		
		return panelCampos;
	}

	private JPanel crearPanelBotones() {
		JPanel panelBotones = new JPanel();
		panelBotones.setBackground(new Color(100, 149, 237));
		panelBotones.setBorder(new EmptyBorder(5, 0, 5, 0));
		panelBotones.setLayout(new BorderLayout(0, 0));

		JPanel panelBotonesLoginRegistro = new JPanel();
		panelBotonesLoginRegistro.setBorder(UIManager.getBorder("MenuItem.border"));
		panelBotonesLoginRegistro.setBackground(new Color(100, 149, 237));
		panelBotones.add(panelBotonesLoginRegistro, BorderLayout.WEST);

		JButton btnLogin = new JButton("Entrar");
		panelBotonesLoginRegistro.add(btnLogin);
		btnLogin.setVerticalAlignment(SwingConstants.BOTTOM);

		JButton btnRegistro = new JButton("Registro");
		panelBotonesLoginRegistro.add(btnRegistro);
		btnRegistro.setVerticalAlignment(SwingConstants.BOTTOM);

		JPanel panelBotonSalir = new JPanel();
		panelBotonSalir.setBorder(UIManager.getBorder("TextArea.border"));
		panelBotonSalir.setBackground(new Color(100, 149, 237));
		panelBotones.add(panelBotonSalir, BorderLayout.EAST);

		JButton btnSalir = new JButton("Salir");
		btnSalir.setVerticalAlignment(SwingConstants.BOTTOM);
		panelBotonSalir.add(btnSalir);

		addManejadorBotonLogin(btnLogin);
		addManejadorBotonRegistro(btnRegistro);
		addManejadorBotonSalir(btnSalir);
		
		return panelBotones;
	}

	private void addManejadorBotonSalir(JButton btnSalir) {
		btnSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmLogin.dispose();
				System.exit(0);
			}
		});
	}

	private void addManejadorBotonRegistro(JButton btnRegistro) {
		btnRegistro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VentanaRegistro registroView = new VentanaRegistro();
				registroView.mostrarVentana();
				frmLogin.dispose();
			}
		});
	}

	private void addManejadorBotonLogin(JButton btnLogin) {
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean login = Controlador.getUnicaInstancia().loginUsuario(textUsuario.getText(),
						new String(textPassword.getPassword()));

				if (login) {
					EventQueue.invokeLater(new Runnable() {
						public void run() {
							try {
								VentanaPrincipal window = new VentanaPrincipal();
								window.mostrarVentana();
								
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
					frmLogin.dispose();
				} else
					JOptionPane.showMessageDialog(frmLogin, "Nombre de usuario o contrase\u00F1a no valido",
							"Error", JOptionPane.ERROR_MESSAGE);
			}
		});
	}

}
