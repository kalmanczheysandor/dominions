package hu.kalmancheysandor.applications.dominions.apis.ai.algorithm.simultan;

//
//public abstract class TTestAlgorithm extends TAiAlgorithm implements ISimultaNAlgorithm {
//    private static final Logger log = LoggerFactory.getLogger(TSimultaNAlgorithm.class);
//    protected GameStateMachine gameStateMachine;
//    private int searchDepthLimit = 0;
//    protected abstract double evaluateHeuristicValueOfGameState(GameState gameState, int supportedPlayerKey);
//    private static int countOfLeafNodes = 0;
//
//    @Override
//    public AiDecisionResult makeDecision(AiDecisionContext context) {
//
//        // Parameter validations
//        assertObjectIsNotNull(context, "context");
//
//        // Gain the best step
//        Integer chosenCellKey = expandState(
//                context.getGameState(), // Initialstate of recursive caluclation
//                context.getYourPlayerKey(), // Supported player key
//                1, // Maximum deepness
//                0  // Parent level deepness
//        ).getAdvisedStepKey();
//
//        System.out.println("countOfLeafNodes: " + countOfLeafNodes);
//
//
//        // Generate return value
//        AiDecisionResult aiDecisionResult;
//        if (chosenCellKey != null) { // when the action is attack
//            aiDecisionResult = new AiDecisionResult(
//                    chosenCellKey,
//                    calculatePlayerMaxAttackPower(
//                            context.getGameState(),
//                            context.getYourPlayerKey()
//                    )
//            );
//        } else {      // When the action is: reserve
//            aiDecisionResult = new AiDecisionResult(0, 0);
//        }
//        return aiDecisionResult;
//    }
//
//    private TSimultaNAlgorithm.TTestAlgorithm expandState(GameState gameState, int supportedPlayerKey, int maxDeepness, int parentLevelDeepness) {
//        // Parameter validations
//        assertObjectIsNotNull(gameState, "gameState");
//        assertIntNotNegative(supportedPlayerKey, "supportedPlayerKey");
//        assertIntIsNotUnderMinimum(maxDeepness, "maxDeepness", 1);
//        assertIntNotNegative(parentLevelDeepness, "parentLevelDeepness");
//
//        //
//        int currentLevel = parentLevelDeepness + 1;
//
//
//        // Collects all alive players' all possible ElementaryOperations, which are applicable in the current game state
//        Set<ElementaryOperator> allElementaryOperatorsOfAlivePlayers = gatherApplicableElementaryOperatorsOfAlivePlayers(gameState);
//
//        // Generate all possible composite operators based on a game state
//        List<CompositeOperator> allCompositeOperators = generateAllCompositeOperators(supportedPlayerKey, allElementaryOperatorsOfAlivePlayers);
//
//        // Split composite operators into target-groups of supported player
//        Map<Integer, List<CompositeOperator>> compositeOperatorsOfNodeGroups = splitCompositeOperatorsIntoNodeGroups(allCompositeOperators, supportedPlayerKey);
//
//        Integer stepKey;
//        List<GameState> stateVariants;
//        double worstHeuristicValue;
//
//        Double currentBestHeuristicValue = null;
//        Integer chosenGroupKey = null;
//
//        for (Map.Entry<Integer, List<CompositeOperator>> nodeGroupEntry : compositeOperatorsOfNodeGroups.entrySet()) {
//            Integer keyOfNode = nodeGroupEntry.getKey();
//            List<CompositeOperator> compositeOperatorsOfNode = nodeGroupEntry.getValue();
//
//            // Generate all possible game state variations based on the composite operators of a node
//            List<GameState> stateVariationsOfNode = generateStateVariationsByCompositeOperator(gameState, compositeOperatorsOfNode);
//
//            // Safety checking: when no game state variants are present in the list, then skip that step
//            if (stateVariationsOfNode.isEmpty()) {
//                continue;
//            }
//
//
//            if ( currentLevel < maxDeepness) {// Go deeper by recursion, heuristic values only delegated from child nodes but not evaluated
//                TSimultaNAlgorithm.ChosenStepData b;
//                List<Double> heuristicValues = new ArrayList<>();
//
//                for (GameState gameStateVariant : stateVariationsOfNode) {
//                    b = expandState(gameStateVariant, supportedPlayerKey, maxDeepness, currentLevel);
//                    heuristicValues.add(b.getDelegatedHeuristicValue());
//                }
//                double heuristicValueOfNode = findTheWorstValue(heuristicValues); // Among variations the worst scenario is the needed value
//
//
//
//                // Always store the elementary operator (of supported player) based on its heuristic value, which has to be better than the previous one.
//                if (currentBestHeuristicValue == null) {
//                    currentBestHeuristicValue = heuristicValueOfNode;
//                    chosenGroupKey = keyOfNode;
//                } else if (currentBestHeuristicValue < heuristicValueOfNode) {
//                    currentBestHeuristicValue = heuristicValueOfNode;
//                    chosenGroupKey = keyOfNode;
//                }
//
//            }
//            else if ( currentLevel == maxDeepness) { // this is a leaf node. here real heuristic evaluation have to calculated
//                countOfLeafNodes++;
//                List<Double> heuristicValues = evaluateHeuristicValueOfGameSateVariants(stateVariationsOfNode, supportedPlayerKey);
//                double heuristicValueOfNode = findTheWorstValue(heuristicValues); // Among variations the worst scenario is the needed value
//
//
//                // Always store the elementary operator (of supported player) based on its heuristic value, which has to be better than the previous one.
//                if (currentBestHeuristicValue == null) {
//                    currentBestHeuristicValue = heuristicValueOfNode;
//                    chosenGroupKey = keyOfNode;
//                } else if (currentBestHeuristicValue < heuristicValueOfNode) {
//                    currentBestHeuristicValue = heuristicValueOfNode;
//                    chosenGroupKey = keyOfNode;
//                }
//
////                System.out.println("New.H.Value:" + currentBestHeuristicValue + " .....chosen:" + chosenGroupKey);
////                System.out.println("----NODE[" + keyOfNode + "].End---");
//            } else {
//                throw new IllegalPointOfExecution("Unexpected case is found!");
//            }
//
//
//        }
//
//        return new TSimultaNAlgorithm.ChosenStepData(chosenGroupKey, currentBestHeuristicValue);
//    }
//
//    private double findTheWorstValue(List<Double> gameStateVariantValues) {
//        // Parameter validations
//        assertObjectIsNotNull(gameStateVariantValues, "gameStateVariantValues");
//        assertListNotEmpty(gameStateVariantValues, "gameStateVariantValues");
//
//        return gameStateVariantValues.stream()
//                .mapToDouble(Double::doubleValue)
//                .min()
//                .orElseThrow();
//    }
//
//    private List<Double> evaluateHeuristicValueOfGameSateVariants(List<GameState> gameStateVariants, int supportedPlayerKey) {
//        // Parameter validations
//        assertObjectIsNotNull(gameStateVariants, "gameStateVariants");
//        assertListNotEmpty(gameStateVariants, "gameStateVariants");
//
//        // Loop - All game state variants inside of supported player step
//        List<Double> heuristicValues = new ArrayList<>();
//        Double currentHeuristicValue;
//        for (GameState gameStateVariant : gameStateVariants) {
//            currentHeuristicValue = evaluateHeuristicValueOfGameState(
//                    gameStateVariant,
//                    supportedPlayerKey
//            );
//            heuristicValues.add(currentHeuristicValue);
//        }
//
//        return heuristicValues;
//    }
//
//
//    /// ////////////////////////////////////////////////////////////////////////////////////////
//    ///  COMPOSITE OPERATORS
//    /// ////////////////////////////////////////////////////////////////////////////////////////
//    private List<CompositeOperator> generateAllCompositeOperators(int supportedPlayerKey, Collection<ElementaryOperator> allElementaryOperatorsOfAlivePlayers) {
//        // Parameter validations
//        assertIntNotNegative(supportedPlayerKey, "supportedPlayerKey");
//
//        return CompositeOperator.createMultiple(
//                supportedPlayerKey,
//                allElementaryOperatorsOfAlivePlayers
//        );
//    }
//
//    private Map<Integer, List<CompositeOperator>> splitCompositeOperatorsIntoNodeGroups(List<CompositeOperator> compositeOperators, int supportedPlayerKey) {
//        // Parameter validations
//        assertObjectIsNotNull(compositeOperators, "compositeOperators");
//        assertIntNotNegative(supportedPlayerKey, "supportedPlayerKey");
//
//        Integer targetedCellKey;
//        List<CompositeOperator> combinationListInAGroup = new ArrayList<>();
//        Map<Integer, List<CompositeOperator>> nodeGroups = new HashMap<>();
//
//        for (CompositeOperator compositeOperator : compositeOperators) {
//
//            // Determine group-key
//            targetedCellKey = compositeOperator.getTargetIndex();
//
//            // Add to related group
//            if (!nodeGroups.containsKey(targetedCellKey)) {
//                List<CompositeOperator> groupItems = new ArrayList<>();
//                groupItems.add(compositeOperator);
//                nodeGroups.put(targetedCellKey, groupItems);
//            } else {
//                List<CompositeOperator> groupItems = nodeGroups.get(targetedCellKey);
//                groupItems.add(compositeOperator);
//            }
//        }
//        return nodeGroups;
//    }
//
//    private GameState applyOneCompositeOperatorOnGameState(GameState initialGameState, CompositeOperator compositeOperator) {
//        // Parameter validations
//        assertObjectIsNotNull(initialGameState, "initialGameState");
//        assertObjectIsNotNull(compositeOperator, "compositeOperator");
//
//
//        // Initialise state-machine
//        if (this.gameStateMachine == null) {
//            this.gameStateMachine = new GameStateMachine();
//        }
//
//        // Collect all intentions
//        Set<GameAction> actions = new HashSet<>();
//        for (ElementaryOperator elementaryOperator : compositeOperator.getCombinations()) {
//            int playerKey = elementaryOperator.getPlayerKey();
//            Integer targetCellKey = elementaryOperator.getTargetIndex();
//
//            int troopSize = getPlayerReserveSize(initialGameState, playerKey);
//            troopSize = (troopSize > TROOPS_SIZE_MAX) ? TROOPS_SIZE_MAX : troopSize;
//            if (targetCellKey == null) {//When reserve action
//                troopSize = 0;
//            }
//            actions.add(new GameAction(playerKey, targetCellKey, troopSize));
//        }
//
//        // Generate new game-state based on the intentions
//        return gameStateMachine.doSteps(actions, GameState.deepCopy(initialGameState));
//    }
//
//    private List<GameState> applyMultipleCompositeOperatorOnGameState(GameState initialGameState, List<CompositeOperator> compositeOperatorsList) {
//        // Parameter validations
//        assertObjectIsNotNull(initialGameState, "initialGameState");
//        assertObjectIsNotNull(compositeOperatorsList, "compositeOperatorsList");
//
//        // Execution
//        List<GameState> gameStateList = new ArrayList<>();
//        for (CompositeOperator compositeOperator : compositeOperatorsList) {
//            gameStateList.add(
//                    applyOneCompositeOperatorOnGameState(
//                            initialGameState,
//                            compositeOperator
//                    )
//            );
//        }
//        return gameStateList;
//    }
//
//    protected List<GameState> generateStateVariationsByCompositeOperator(GameState gameState, List<CompositeOperator> compositeOperatorsList) {
//        return applyMultipleCompositeOperatorOnGameState(gameState, compositeOperatorsList);
//    }
//
//    /// ////////////////////////////////////////////////////////////////////////////////////////
//    ///  ELEMENTARY OPERATORS
//    /// ////////////////////////////////////////////////////////////////////////////////////////
//    protected Set<ElementaryOperator> gatherApplicableElementaryOperatorsOfAPlayer(GameState gameState, Integer playerKey) {
//        // Parameter validations
//        assertObjectIsNotNull(gameState, "gameState");
//        assertIntNotNegative(playerKey, "playerKey");
//
//        //Adding attack steps
//        Set<ElementaryOperator> elementaryOperators = new HashSet<>();
//        for (Integer cellKey : collectCellKeysOfCurrentAttackZoneOfPlayer(gameState, playerKey)) {
//            elementaryOperators.add(new ElementaryOperator(playerKey, cellKey));
//        }
//        // Adding the "reserve" step
//        elementaryOperators.add(new ElementaryOperator(playerKey, null));
//
//        return elementaryOperators;
//    }
//
//    protected Set<ElementaryOperator> gatherApplicableElementaryOperatorsOfAlivePlayers(GameState gameState) {
//        // Parameter validations
//        assertObjectIsNotNull(gameState, "gameState");
//
//        // All players key
//        Set<Integer> alivePlayerKeys = collectAllAlivePlayerKeys(gameState);
//
//        //
//        Set<ElementaryOperator> output = new HashSet<>();
//        for (Integer playerKey : alivePlayerKeys) {
//            output.addAll(gatherApplicableElementaryOperatorsOfAPlayer(gameState, playerKey));
//        }
//
//        return output;
//    }
//
//
//
//
//
//
//
//
//    /// ////////////////////////////////////////////////////////////////////////////////////////
//    ///  ETC
//    /// ////////////////////////////////////////////////////////////////////////////////////////
//    public void setSearchDepthLimit(int searchDepthLimit) {
//        // Parameter checking
//        assertIntNotNegative(searchDepthLimit, "searchDepthLimit");
//
//        this.searchDepthLimit = searchDepthLimit;
//    }
//
//    /// ////////////////////////////////////////////////////////////////////////////////////////
//    /// // Inner Classes /////////////////////////////////////////////////////////////////////
//    /// ////////////////////////////////////////////////////////////////////////////////////////
//    @Data
//    @NoArgsConstructor
//    @AllArgsConstructor
//    private static class ChosenStepData {
//        Integer advisedStepKey;
//        double delegatedHeuristicValue;
//
//    }
//
//
////
////
////    private List<Double> evaluateHeuristicValueOfGameSateVariants(List<GameState> gameStateVariants, int supportedPlayerKey, int currentLevel, int maxDeepness) {
////        // Parameter validations
////        assertObjectIsNotNull(gameStateVariants, "gameStateVariants");
////        assertListNotEmpty(gameStateVariants, "gameStateVariants");
////        assertIntNotNegative(supportedPlayerKey, "supportedPlayerKey");
////        assertIntNotNegative(maxDeepness, "maxDeepness");
////        assertIntNotNegative(currentLevel, "currentLevel");
////
////        // Loop - All game state variants inside of supported player step
////        List<Double> heuristicValues = new ArrayList<>();
////        Double currentHeuristicValue;
////        Double worstHeuristicValue = null;
////        for (GameState gameStateVariant : gameStateVariants) {
////
////            if (maxDeepness > currentLevel) {// Go deeper by recursion, heuristic values only delegated from child nodes but not evaluated
////                currentHeuristicValue = chooseTheBestStep(
////                        gameStateVariant,
////                        supportedPlayerKey,
////                        maxDeepness,
////                        currentLevel
////                ).getHeuristicValue();
////
////            } else if (maxDeepness == currentLevel) { // this is a leaf node. here real heuristic evaluation have to calculated
////                currentHeuristicValue = evaluateHeuristicValueOfGameState(
////                        gameStateVariant,
////                        supportedPlayerKey
////                );
////            } else {
////                throw new IllegalPointOfExecution("Unexpected case is found!");
////            }
////
////            heuristicValues.add(currentHeuristicValue);
////        }
////
////        return heuristicValues;
////    }
//
////    protected Map<Integer, List<GameState>> generateStateVariationsOfNodeGroups(GameState gameState, Map<Integer, List<Map<Integer, Integer>>> groupOfCompositeOperatorsList) {
////
////        // Parameter validations
////        assertObjectIsNotNull(gameState, "gameState");
////        assertObjectIsNotNull(groupOfCompositeOperatorsList, "groupOfCompositeOperatorsList");
////
////        //
////        Integer groupKey;
////        List<Map<Integer, Integer>> compositeOperatorsList;
////        Map<Integer, List<GameState>> gameStateListGroups = new HashMap<>();
////        for (Map.Entry<Integer, List<Map<Integer, Integer>>> groupEntry : groupOfCompositeOperatorsList.entrySet()) {
////            // Initialisation
////            groupKey = groupEntry.getKey();
////            compositeOperatorsList = groupEntry.getValue();
////
////            //
////            gameStateListGroups.put(
////                    groupKey,
////                    applyMultipleCompositeOperatorOnGameState(gameState, compositeOperatorsList)
////            );
////        }
////
////        return gameStateListGroups;
////    }
//
//
////
////
////
////    private BranchData chooseTheBestStep(GameState gameState, int supportedPlayerKey, int maxDeepness, int parentLevelDeepness) {
////        // Parameter validations
////        assertObjectIsNotNull(gameState, "gameState");
////        assertIntNotNegative(supportedPlayerKey, "supportedPlayerKey");
////        assertIntIsNotUnderMinimum(maxDeepness, "maxDeepness", 1);
////        assertIntNotNegative(parentLevelDeepness, "parentLevelDeepness");
////
////        //
////        int currentLevel = parentLevelDeepness + 1;
////
////
////        // Generate all possible composite operators based on a game state
////        List<Map<Integer, Integer>> allCompositeOperators = generateAllCompositeOperators(gameState);
////        log.info("All-composite-operator:{}", allCompositeOperators);
////
////        // Split composite operators into node-groups of supported player
////        Map<Integer, List<Map<Integer, Integer>>> compositeOperatorsOfNodeGroups = splitCompositeOperatorsIntoNodeGroups(allCompositeOperators, supportedPlayerKey);
////        log.info("All-composite-operator-Grouop:{}", compositeOperatorsOfNodeGroups);
////
////        // Step 2
////        Map<Integer, List<GameState>> stateVariationsOfNodeGroups = generateStateVariationsOfNodeGroups(gameState, compositeOperatorsOfNodeGroups);
////
////        // Step 3
////        Integer stepKey;
////        List<GameState> stateVariants;
////        double worstHeuristicValue;
////        Double theLessWorse = null;
////        Integer chosenKey = null;
////
////        for (Map.Entry<Integer, List<GameState>> entry : stateVariationsOfNodeGroups.entrySet()) { // Egy allapotra alkalmazott csomopontok(allapothalmazok) bejarasa
////            stepKey = entry.getKey();       // A tamogatott jatekos szeinti operator alkalmazas
////            stateVariants = entry.getValue();   // Az osszes allapot varians listaja
////
////            // Safety checking: when no game state variants are present in the list, then skip that step
////            if (stateVariants.isEmpty()) {
////                continue;
////            }
////
////            // Evaluate all game state variants and retrieve the value of the worst one.
////            List<Double> values = evaluateHeuristicValueOfGameSateVariants(stateVariants, supportedPlayerKey, currentLevel, maxDeepness);
////            worstHeuristicValue = modulateHeuristicValues(values);
////
////
////            // Always store that value which is less wors than the previous one
////            if (theLessWorse == null) {
////                theLessWorse = worstHeuristicValue;
////                chosenKey = stepKey;
////            } else if (theLessWorse < worstHeuristicValue) {
////                theLessWorse = worstHeuristicValue;
////                chosenKey = stepKey;
////            }
////
////            System.out.println(">".repeat(currentLevel) + " ObservedCellKey:" + stepKey + " H:" + worstHeuristicValue + " .....chosen:" + chosenKey);
////        }
////
////        return new BranchData(chosenKey, theLessWorse);
////    }
//}
