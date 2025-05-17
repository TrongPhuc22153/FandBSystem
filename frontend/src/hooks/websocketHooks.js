import { useEffect } from "react";
import { useWebSocket } from "../context/WebSocketContext";

export const useStompSubscription = ({ topic, onMessage, shouldSubscribe }) => {
    const { client, connected } = useWebSocket();

    useEffect(() => {
        if (!shouldSubscribe || !connected || !client || !topic || !onMessage) return;

        const subscription = client.subscribe(topic, message => {
            if (message.body) {
                onMessage(JSON.parse(message.body));
            }
        });

        return () => {
            subscription.unsubscribe();
        };
    }, [client, connected, topic, onMessage, shouldSubscribe]);
};