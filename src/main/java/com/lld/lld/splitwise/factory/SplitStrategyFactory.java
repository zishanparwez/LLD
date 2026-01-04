package com.lld.lld.splitwise.factory;

import com.lld.lld.splitwise.enums.SplitMethod;
import com.lld.lld.splitwise.strategy.EqualSplitStrategy;
import com.lld.lld.splitwise.strategy.ExactSplitStrategy;
import com.lld.lld.splitwise.strategy.PercentageSplitStrategy;
import com.lld.lld.splitwise.strategy.SplitStrategy;

public class SplitStrategyFactory {

    private static SplitStrategyFactory factoryInstance;

    public static synchronized SplitStrategyFactory getFactoryInstance() {
        if(factoryInstance == null) {
            factoryInstance = new SplitStrategyFactory();
        }
        return factoryInstance;
    }

    public SplitStrategy createSplitFactory(SplitMethod splitMethod) {
        switch (splitMethod) {
            case EQUAL:
                return new EqualSplitStrategy();
            case EXACT:
                return new ExactSplitStrategy();
            case PERCENTAGE:
                return new PercentageSplitStrategy();
            default:
                break;
        }
        return null;
    }
}
