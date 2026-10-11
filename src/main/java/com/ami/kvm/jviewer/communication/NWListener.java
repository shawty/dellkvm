package com.ami.kvm.jviewer.communication;

import com.ami.kvm.jviewer.Debug;
import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.Selector;

public class NWListener implements Runnable {
    private Client m_clnt;
    private boolean m_run = false;
    private Selector m_select = null;

    public NWListener() {
        try {
            this.m_select = Selector.open();
        } catch (IOException e) {
            Debug.out.println(e);
        }
        this.m_run = true;
    }

    public int addChannel(Client client) {
        try {
            client.getSockChannel().register(this.m_select, 1);
            this.m_clnt = client;
            return 0;
        } catch (ClosedChannelException e) {
            Debug.out.println(e);
            return -1;
        }
    }

    public void removeChannel(Client client) {
        client.getSockChannel().keyFor(this.m_select).cancel();
    }

    public void startListener() {
        this.m_run = true;
    }

    public void destroyListener() {
        this.m_run = false;
        this.m_select.wakeup();
        try {
            this.m_select.selectNow();
        } catch (Exception e) {
        }
    }

    public void run() {
        while (this.m_run) {
            try {
                this.m_select.select();
            } catch (IOException e) {
                Debug.out.println("NW_LISTENER " + e);
            }
            if (this.m_run) {
                this.m_clnt.onReadEvent();
            }
        }
    }
}
