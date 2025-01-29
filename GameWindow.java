import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class GameWindow extends JFrame {
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    private static final int CHARACTER_SIZE = 50;
    
    private BattlePanel battlePanel;
    private JButton nextRoundButton;
    private AutoBattler autoBattler;
    private JLabel roundLabel;
    
    public GameWindow() {
        autoBattler = new AutoBattler();
        initializeWindow();
        initializeGame();
    }
    
    private void initializeWindow() {
        setTitle("Auto Battler");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Create battle panel
        battlePanel = new BattlePanel();
        add(battlePanel, BorderLayout.CENTER);
        
        // Create control panel
        JPanel controlPanel = new JPanel();
        nextRoundButton = new JButton("Start Next Round");
        roundLabel = new JLabel("Round: 1");
        roundLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        nextRoundButton.addActionListener(e -> startNextRound());
        
        controlPanel.add(roundLabel);
        controlPanel.add(nextRoundButton);
        add(controlPanel, BorderLayout.SOUTH);
    }
    
    private void initializeGame() {
        autoBattler.initializePlayerTeam();
        autoBattler.generateEnemyTeam();
        battlePanel.updateTeams(autoBattler.getPlayerTeam(), autoBattler.getEnemyTeam());
        roundLabel.setText("Round: " + autoBattler.getRound());
    }
    
    private void startNextRound() {
        nextRoundButton.setEnabled(false);
        Thread battleThread = new Thread(() -> {
            boolean wonRound = autoBattler.simulateRound(battlePanel);
            if (wonRound) {
                autoBattler.generateEnemyTeam();
                battlePanel.updateTeams(autoBattler.getPlayerTeam(), autoBattler.getEnemyTeam());
                roundLabel.setText("Round: " + autoBattler.getRound());
                nextRoundButton.setEnabled(true);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Game Over! You reached round " + autoBattler.getRound(), 
                    "Game Over", 
                    JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            }
        });
        battleThread.start();
    }
    
    class BattlePanel extends JPanel {
        private Character[] playerTeam;
        private Character[] enemyTeam;
        private Map<Character, Point> characterPositions;
        private Map<Character, Color> characterColors;
        private List<DamageNumber> damageNumbers;
        private static final int DAMAGE_NUMBER_DURATION = 1000; // milliseconds
        
        public BattlePanel() {
            characterPositions = new HashMap<>();
            characterColors = new HashMap<>();
            damageNumbers = new ArrayList<>();
            setBackground(new Color(240, 240, 240)); // Light gray background
        }
        
        public void updateTeams(Character[] playerTeam, Character[] enemyTeam) {
            this.playerTeam = playerTeam;
            this.enemyTeam = enemyTeam;
            updatePositions();
            repaint();
        }
        
        private void updatePositions() {
            characterPositions.clear();
            characterColors.clear();
            
            // Position player team on the left
            for (int i = 0; i < playerTeam.length; i++) {
                if (playerTeam[i] != null && playerTeam[i].isAlive()) {
                    characterPositions.put(playerTeam[i], 
                        new Point(100, 150 + i * 150));
                    characterColors.put(playerTeam[i], getColorForType(playerTeam[i].getType()));
                }
            }
            
            // Position enemy team on the right
            for (int i = 0; i < enemyTeam.length; i++) {
                if (enemyTeam[i] != null && enemyTeam[i].isAlive()) {
                    characterPositions.put(enemyTeam[i], 
                        new Point(WINDOW_WIDTH - 150, 150 + i * 150));
                    characterColors.put(enemyTeam[i], getColorForType(enemyTeam[i].getType()));
                }
            }
        }
        
        private Color getColorForType(Character.CharacterType type) {
            switch (type) {
                case WARRIOR: return new Color(220, 50, 50);  // Brighter red
                case ARCHER: return new Color(50, 180, 50);   // Brighter green
                case MAGE: return new Color(50, 50, 220);     // Brighter blue
                default: return Color.GRAY;
            }
        }
        
        public void animateAttack(Character attacker, Character target) {
            if (!characterPositions.containsKey(attacker) || !characterPositions.containsKey(target)) {
                return;
            }
            
            Point start = characterPositions.get(attacker);
            Point end = characterPositions.get(target);
            
            // Add damage number
            int damage = attacker.getAttack();
            damageNumbers.add(new DamageNumber(damage, end.x, end.y, System.currentTimeMillis()));
            
            // Create projectile animation
            Thread animationThread = new Thread(() -> {
                Point current = new Point(start);
                int steps = 10;
                int dx = (end.x - start.x) / steps;
                int dy = (end.y - start.y) / steps;
                
                for (int i = 0; i < steps; i++) {
                    current.translate(dx, dy);
                    repaint();
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                
                // Flash the target character
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                repaint();
            });
            
            animationThread.start();
            try {
                animationThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Draw team labels
            g2d.setFont(new Font("Arial", Font.BOLD, 24));
            g2d.setColor(new Color(0, 100, 200));
            g2d.drawString("YOUR TEAM", 50, 50);
            g2d.setColor(new Color(200, 0, 0));
            g2d.drawString("ENEMY TEAM", WINDOW_WIDTH - 200, 50);
            
            // Draw characters
            characterPositions.forEach((character, position) -> {
                if (character.isAlive()) {
                    // Draw character
                    g2d.setColor(characterColors.get(character));
                    g2d.fillOval(position.x, position.y, CHARACTER_SIZE, CHARACTER_SIZE);
                    g2d.setColor(Color.BLACK);
                    g2d.drawOval(position.x, position.y, CHARACTER_SIZE, CHARACTER_SIZE);
                    
                    // Draw HP bar
                    int hpBarWidth = 100;
                    int hpBarHeight = 10;
                    double hpRatio = (double) character.getHp() / character.getMaxHp();
                    g2d.setColor(new Color(180, 0, 0));
                    g2d.fillRect(position.x - 25, position.y - 20, hpBarWidth, hpBarHeight);
                    g2d.setColor(new Color(0, 180, 0));
                    g2d.fillRect(position.x - 25, position.y - 20, 
                        (int)(hpBarWidth * hpRatio), hpBarHeight);
                    
                    // Draw HP numbers
                    g2d.setFont(new Font("Arial", Font.BOLD, 12));
                    g2d.setColor(Color.BLACK);
                    String hpText = character.getHp() + "/" + character.getMaxHp() + " HP";
                    g2d.drawString(hpText, position.x - 20, position.y - 25);
                    
                    // Draw character type and stats
                    g2d.setFont(new Font("Arial", Font.BOLD, 14));
                    String statsText = character.getType().toString() + " (ATK: " + character.getAttack() + ")";
                    g2d.drawString(statsText, position.x - 20, position.y + CHARACTER_SIZE + 20);
                }
            });
            
            // Draw damage numbers
            long currentTime = System.currentTimeMillis();
            Iterator<DamageNumber> iterator = damageNumbers.iterator();
            while (iterator.hasNext()) {
                DamageNumber damageNumber = iterator.next();
                if (currentTime - damageNumber.startTime > DAMAGE_NUMBER_DURATION) {
                    iterator.remove();
                } else {
                    float alpha = 1.0f - (currentTime - damageNumber.startTime) / (float)DAMAGE_NUMBER_DURATION;
                    g2d.setFont(new Font("Arial", Font.BOLD, 20));
                    g2d.setColor(new Color(1.0f, 0.0f, 0.0f, alpha));
                    g2d.drawString("-" + damageNumber.damage, 
                        damageNumber.x + CHARACTER_SIZE/2, 
                        damageNumber.y - 20 - (int)((1-alpha) * 30));
                }
            }
        }
        
        private class DamageNumber {
            int damage;
            int x, y;
            long startTime;
            
            DamageNumber(int damage, int x, int y, long startTime) {
                this.damage = damage;
                this.x = x;
                this.y = y;
                this.startTime = startTime;
            }
        }
    }
}
