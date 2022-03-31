import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable, KeyListener {
	
	private static final long serialVersionUID = 1L;
	public static final int width = 500, height = 500; 
	private Thread thread;
	private boolean running;
	private boolean right = true, left = false, up = false, down = false;
	private BodyPart b;
	private ArrayList<BodyPart> snake;
	private Apple apple;
	private ArrayList<Apple>apples;
	private Random r;
	private int xCoor = 10, yCoor = 10, size = 5;
	private int ticks = 0;

	public GamePanel() {
		setFocusable(true);
		setPreferredSize(new Dimension(width, height));
		addKeyListener(this);
		snake = new ArrayList <BodyPart>();
		apples = new ArrayList<Apple>();
		r = new Random();
		
		start();
	}

	public void start() {
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	
	public void stop() {
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void tick() {
		if(snake.size() == 0) {
			b = new BodyPart(xCoor, yCoor, 0);
			snake.add(b);
		}
		ticks++;
		if(ticks > 300000) {
			if(right) xCoor++;
			if(left) xCoor--;
			if(up) yCoor--;
			if(down) yCoor++;
			
			ticks = 0;
			
			b = new BodyPart(xCoor, yCoor, 15);
			snake.add(b);
			
			if(snake.size() > size) {
				snake.remove(0);
			}
		}
		if(apples.size() == 0) {
			int xCoor = r.nextInt(32);
			int yCoor = r.nextInt(32);
			
			apple = new Apple(xCoor, yCoor, 15);
			apples.add(apple);
		}
		
		for(int i = 0; i < apples.size(); i++) {
			if(xCoor == apples.get(i).getxCoor() && yCoor == apples.get(i).getyCoor()) {
				size++;
				apples.remove(i);
				i++;
			}
		}
		
		for(int i = 0; i < snake.size(); i++) {
			if(xCoor == snake.get(i).getxCoor() && yCoor == snake.get(i).getyCoor()) {
				if(i != snake.size() - 1) {
					System.out.println("Game over");
					stop();
				}
			}
		}
		
		if(xCoor < 0 || xCoor > 32 || yCoor < 0 || yCoor > 32) {
			System.out.println("Game over");
			stop();
			
		}
	}
	
	public void paint(Graphics g) {
		g.clearRect(0, 0, width, height);
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);
		
		for(int i = 0; i < width/10; i++) {
			g.drawLine(i * 10, 0, i * 10, height);
		}
		for(int i = 0; i < height/10; i++) {
			g.drawLine(0, i * 10, width, i * 10);
		}
		for(int i = 0; i < snake.size(); i++) {
			snake.get(i).draw(g);
		}
		for(int i = 0; i < apples.size(); i++) {
			apples.get(i).draw(g);
		}
	}

	@Override
	public void run() {
		while(running) {
			tick();
			repaint();
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if(key == KeyEvent.VK_RIGHT && !left) {
			right = true;
			up = false;
			down = false;
		}
		if(key == KeyEvent.VK_LEFT && !right) {
			left = true;
			up = false;
			down = false;
		}
		if(key == KeyEvent.VK_UP && !down) {
			up = true;
			right = false;
			left = false;
		}
		if(key == KeyEvent.VK_DOWN && !up) {
			down = true;
			right = false;
			left = false;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}
}
