    package poker;
    import java.util.ArrayList;


    /*
    All game logic in this class
    */
    public class PokerGame {
        private Pot pot;
        private Dealer dealer;
        private ArrayList<Player> players;
        private CommunityHand board;
        private int dealerIndex = 0;


        public PokerGame(ArrayList<Player> players){
            this.pot = new Pot();
            this.dealer = new Dealer();
            this.players = new ArrayList<Player>(players);
            this.board = new CommunityHand();

        }

        /* in poker, the dealer rotates every round
        small blind bet = 1 chip
        big blind bet = 2 chips
        */

        private void rotateDealer(){
            this.dealerIndex = (this.dealerIndex + 1) % this.players.size();
        }

        private int getSmallBlindIndex(){
            return (this.dealerIndex + 1) % this.players.size();
        }

        private int getBigBlindIndex(){
            return (this.dealerIndex + 2) % this.players.size();
        }

        private int getFTAIndex(){
            return (this.dealerIndex + 3) % this.players.size();
        }

        private int getHighestBet(){
            int max = 0;
            for(Player p : players){
                if(p.getCurrentBet() > max){
                    max = getHighestBet();
                }
            }
            return max;
        }

        public void runBettingRound(int startingPlayerIndex){
            int highestBet = getHighestBet();

            int currentIndex = startingPlayerIndex;

            for(int i = 0; i < players.size(); i++){
                Player p = players.get(currentIndex);
                if(p.hasFolded()){
                    currentIndex = (currentIndex + 1) % players.size();
                    continue;
                }

                int ToCall = highestBet - p.getCurrentBet();

                //for now players automatically call, later will add the ability to fold or raise
                if(ToCall > 0){
                    p.bet(ToCall);
                    pot.addToPot(ToCall);
                } //TODO Stuff for user input, will have to make another class or function that handles it all

                currentIndex = (currentIndex + 1) % players.size();
            }
            

            
        }

        public void PreFlop(){
            //reset everything for new hand
            this.pot = new Pot();
            this.board = new CommunityHand();
            this.dealer = new Dealer();

            //reset all players
            for(Player p : players){
                p.resetPlayer();
            }

            rotateDealer();

            int sbPlayer = getSmallBlindIndex();
            int bbPlayer = getBigBlindIndex();

            this.players.get(sbPlayer).bet(1);
            this.players.get(bbPlayer).bet(2);
            //TODO LATER: Handle a scenario where player doesn't have enough chips

            this.pot.addToPot(3);

            dealer.dealHands(players);

            int startingPlayer = getFTAIndex();

            //TODO Betting round stuff

        }

        public void resetGame(){
            this.pot = new Pot();
            this.dealer = new Dealer();
        }
    }
