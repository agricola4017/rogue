public class AutoBattler {
    private static final int TEAM_SIZE = 3;
    private Character[] playerTeam;
    private Character[] enemyTeam;
    private int round;

    public AutoBattler() {
        this.playerTeam = new Character[TEAM_SIZE];
        this.enemyTeam = new Character[TEAM_SIZE];
        this.round = 1;
    }

    public void initializePlayerTeam() {
        playerTeam[0] = new Character(Character.CharacterType.WARRIOR, 0);
        playerTeam[1] = new Character(Character.CharacterType.ARCHER, 1);
        playerTeam[2] = new Character(Character.CharacterType.MAGE, 2);
    }

    public void generateEnemyTeam() {
        for (int i = 0; i < TEAM_SIZE; i++) {
            enemyTeam[i] = Character.generateEnemy(round, i + 3);
        }
    }

    public boolean simulateRound(GameWindow.BattlePanel battlePanel) {
        System.out.println("\n=== Round " + round + " ===");
        printTeams();
        
        boolean battleContinues = true;
        while (battleContinues) {
            // Player team attacks
            for (Character attacker : playerTeam) {
                if (attacker != null && attacker.isAlive()) {
                    Character target = attacker.findTarget(enemyTeam);
                    if (target != null) {
                        battlePanel.animateAttack(attacker, target);
                        target.takeDamage(attacker.getAttack());
                        System.out.println(attacker.getType() + " attacks " + target.getType() + 
                                " for " + attacker.getAttack() + " damage!");
                        battlePanel.repaint();
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            // Enemy team attacks
            for (Character attacker : enemyTeam) {
                if (attacker != null && attacker.isAlive()) {
                    Character target = attacker.findTarget(playerTeam);
                    if (target != null) {
                        battlePanel.animateAttack(attacker, target);
                        target.takeDamage(attacker.getAttack());
                        System.out.println("Enemy " + attacker.getType() + " attacks " + 
                                target.getType() + " for " + attacker.getAttack() + " damage!");
                        battlePanel.repaint();
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            // Check if battle is over
            if (!isTeamAlive(enemyTeam)) {
                System.out.println("\nVictory! Your team won round " + round + "!");
                upgradeTeam();
                round++;
                return true;
            }
            if (!isTeamAlive(playerTeam)) {
                System.out.println("\nDefeat! Your team was defeated in round " + round + "!");
                return false;
            }
        }
        return false;
    }

    private boolean isTeamAlive(Character[] team) {
        for (Character character : team) {
            if (character != null && character.isAlive()) {
                return true;
            }
        }
        return false;
    }

    private void upgradeTeam() {
        for (Character character : playerTeam) {
            if (character != null && character.isAlive()) {
                character.atkUp();
                character.hpUp();
                System.out.println(character.getType() + " grew stronger!");
            }
        }
    }

    private void printTeams() {
        System.out.println("\nYour Team:");
        for (Character character : playerTeam) {
            if (character != null) {
                System.out.println("  " + character.toString());
            }
        }
        
        System.out.println("\nEnemy Team:");
        for (Character character : enemyTeam) {
            if (character != null) {
                System.out.println("  " + character.toString());
            }
        }
        System.out.println();
    }

    public int getRound() {
        return round;
    }

    public Character[] getPlayerTeam() {
        return playerTeam;
    }

    public Character[] getEnemyTeam() {
        return enemyTeam;
    }
}
