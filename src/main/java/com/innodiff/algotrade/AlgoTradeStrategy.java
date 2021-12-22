package com.innodiff.algotrade;

import com.dukascopy.api.*;
import com.dukascopy.api.feed.IFeedDescriptor;
import com.dukascopy.api.feed.IFeedListener;
import com.dukascopy.api.feed.util.TicksFeedDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * @author m.monteiro
 */

public class AlgoTradeStrategy implements IStrategy, IFeedListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(AlgoTradeStrategy.class);

    @Configurable("")
    IFeedDescriptor ticksFeedDescriptor =
            new TicksFeedDescriptor(
                    Instrument.EURUSD
            );
    @Configurable("selectedInstrument:")
    public Instrument selectedInstrument = Instrument.EURUSD;
    private IEngine engine;
    private IConsole console;
    private IHistory history;
    private IContext context;

    @Override
    public void onStart(IContext context) throws JFException {
        this.engine = context.getEngine();
        this.console = context.getConsole();
        this.history = context.getHistory();
        this.context = context;

        // Set<Instrument> instruments = new HashSet<Instrument>();
        // instruments.add(selectedInstrument);
        // context.setSubscribedInstruments(instruments, true);

        context.setSubscribedInstruments(
                java.util.Collections.singleton(ticksFeedDescriptor.getInstrument()), true);
        context.subscribeToFeed(ticksFeedDescriptor, this);
        selectedInstrument = ticksFeedDescriptor.getInstrument();
        console.getOut().println("Hello AlgoTrader, isTradable: "+engine.isTradable(selectedInstrument));
    }

    @Override
    public void onTick(Instrument instrument, ITick tick) throws JFException {
        if (!instrument.equals(selectedInstrument)) {
            return;
        }
        LOGGER.info("OnTick: "+instrument.getName()+",ask:"+tick.getAsk());
    }

    @Override
    public void onBar(Instrument instrument, Period period, IBar askBar, IBar bidBar) throws JFException {
    }

    @Override
    public void onMessage(IMessage message) throws JFException {
        console.getOut().println(message);
    }

    @Override
    public void onAccount(IAccount account) throws JFException {
    }

    @Override
    public void onStop() throws JFException {
    }

    @Override
    public void onFeedData(IFeedDescriptor feedDescriptor, ITimedData feedData) {
        console.getOut().println("feed completed: " + feedData + " of feed: " + feedDescriptor);
    }
}
