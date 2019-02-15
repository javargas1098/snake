package snakepackage;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;

import enums.GridSize;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.management.monitor.Monitor;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * @author jd-
 *
 */
public class SnakeApp {

	private static SnakeApp app;
	public static final int MAX_THREADS = 8;
	Snake[] snakes = new Snake[MAX_THREADS];
	private static final Cell[] spawn = { new Cell(1, (GridSize.GRID_HEIGHT / 2) / 2),
			new Cell(GridSize.GRID_WIDTH - 2, 3 * (GridSize.GRID_HEIGHT / 2) / 2),
			new Cell(3 * (GridSize.GRID_WIDTH / 2) / 2, 1),
			new Cell((GridSize.GRID_WIDTH / 2) / 2, GridSize.GRID_HEIGHT - 2),
			new Cell(1, 3 * (GridSize.GRID_HEIGHT / 2) / 2),
			new Cell(GridSize.GRID_WIDTH - 2, (GridSize.GRID_HEIGHT / 2) / 2),
			new Cell((GridSize.GRID_WIDTH / 2) / 2, 1),
			new Cell(3 * (GridSize.GRID_WIDTH / 2) / 2, GridSize.GRID_HEIGHT - 2) };
	private JFrame frame;
	private static Board board;
	int nr_selected = 0;

	Thread[] thread = new Thread[MAX_THREADS];
	
	//fragmento de codigo realziado por javierVargas y SebastianGoenaga
	public SnakeApp() {
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		frame = new JFrame("The Snake Race");
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// frame.setSize(618, 640);
		frame.setSize(GridSize.GRID_WIDTH * GridSize.WIDTH_BOX + 17, GridSize.GRID_HEIGHT * GridSize.HEIGH_BOX + 40);
		frame.setLocation(dimension.width / 2 - frame.getWidth() / 2, dimension.height / 2 - frame.getHeight() / 2);
		board = new Board();
		frame.add(board, BorderLayout.CENTER);

		JPanel actionsBPabel = new JPanel();
		actionsBPabel.setLayout(new FlowLayout());
		JButton btn1;
		JButton btn2;
		JButton btn3;
		actionsBPabel.add(btn3 = new JButton("start "));
		btn3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (int i = 0; i != MAX_THREADS; i++) {
					if (!thread[i].isAlive()) {
						thread[i].start();
					}

				}

			}
		});
		actionsBPabel.add(btn1 = new JButton("pausa "));
		btn1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int maxsnake = 0;
				int cambio = 0;
				int idsnake = 0;
				int idDeadSnake = -1;
				for (Snake snake : snakes) {
					snake.pausa();
					cambio = snake.getBody().size();
					if (cambio > maxsnake) {
						maxsnake = cambio;
						idsnake = snake.getIdt();

					}
					if (snake.isSnakeEnd()) {
						idDeadSnake = snake.getIdt();
					}

				}
				JFrame aviso = new JFrame();
				JPanel panel1 = new JPanel();
				JLabel label = new JLabel("La mas grande es " + idsnake + " con tamaño de " + maxsnake);
				JLabel label2;
				if (idDeadSnake != -1) {
					label2 = new JLabel("La primera que murio fue: " + idDeadSnake);
				} else {
					label2 = new JLabel("Aun no hay muertas");
				}

				panel1.add(label);
				panel1.add(label2);
				aviso.add(panel1);
				aviso.setSize(250, 250);
				aviso.setVisible(true);

			}
		});
		actionsBPabel.add(btn2 = new JButton("resume "));
		btn2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (Snake snake : snakes) {
					snake.resumeGame();

				}

			}
		});

		frame.add(actionsBPabel, BorderLayout.SOUTH);

	}

	public static void main(String[] args) {
		app = new SnakeApp();
		app.init();
	}

	private void init() {

		for (int i = 0; i != MAX_THREADS; i++) {

			snakes[i] = new Snake(i + 1, spawn[i], i + 1);
			snakes[i].addObserver(board);
			thread[i] = new Thread(snakes[i]);
//			thread[i].start();
		}

		frame.setVisible(true);

		while (true) {
			int x = 0;
			for (int i = 0; i != MAX_THREADS; i++) {
				if (snakes[i].isSnakeEnd() == true) {
					x++;
				}
			}
			if (x == MAX_THREADS) {
				break;
			}
		}

		System.out.println("Thread (snake) status:");
		for (int i = 0; i != MAX_THREADS; i++) {
			System.out.println("[" + i + "] :" + thread[i].getState());
		}

	}

	public static SnakeApp getApp() {
		return app;
	}

}
