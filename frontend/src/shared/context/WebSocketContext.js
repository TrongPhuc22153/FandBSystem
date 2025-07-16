import { createContext, useContext, useEffect, useRef, useState } from 'react';
import { Client } from '@stomp/stompjs';
import { WEBSOCKET_ENDPOINT } from '../constants/api';
import { useAuth } from './AuthContext';

const WebSocketContext = createContext(null);

const WebSocketProvider = ({ children }) => {
    const clientRef = useRef(null);
    const [connected, setConnected] = useState(false);
    const { token } = useAuth();

    useEffect(() => {
        const client = new Client({
            brokerURL: WEBSOCKET_ENDPOINT,
            reconnectDelay: 5000,
            connectHeaders: {
                Authorization: `Bearer ${token}`,
            },
            onConnect: () => {
                setConnected(true);
            },
            onDisconnect: () => {
                console.log('Disconnected');
                setConnected(false);
            },
             onStompError: (frame) => {
                console.error('STOMP protocol error:', frame.headers.message);
                // You could handle specific STOMP errors here
            },
        });

        client.activate();
        clientRef.current = client;

        return () => {
            client.deactivate();
        };
    }, [token]);

    return (
        <WebSocketContext.Provider value={{ client: clientRef.current, connected }}>
            {children}
        </WebSocketContext.Provider>
    );
};

export default WebSocketProvider;

export const useWebSocket = () => useContext(WebSocketContext);
