import React, { useState, useRef, useEffect } from 'react';
import api from '../utils/api';
import styles from './Chat.module.css';

interface Msg {
  id: string;
  role: 'user' | 'ai';
  text: string;
  time?: string;
}

const Chat: React.FC = () => {
  const [input, setInput] = useState('');
  const [msgs, setMsgs] = useState<Msg[]>([]);
  const [sending, setSending] = useState(false);
  const bottomRef = useRef<HTMLDivElement | null>(null);

  // Auto-scroll whenever messages change
  useEffect(() => {
    bottomRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [msgs]);

  async function send() {
    if (!input.trim() || sending) return;

    const text = input.trim();
    setInput('');
    const userMsg: Msg = { id: crypto.randomUUID(), role: 'user', text };
    setMsgs((m) => [...m, userMsg]);
    setSending(true);

    try {
      const { data } = await api.post('/chat', { message: text });
      const aiMsg: Msg = {
        id: crypto.randomUUID(),
        role: 'ai',
        text: data.response,
        time: data.timestamp,
      };
      setMsgs((m) => [...m, aiMsg]);
    } catch (e: any) {
      const aiMsg: Msg = {
        id: crypto.randomUUID(),
        role: 'ai',
        text: 'Error contacting server.',
      };
      setMsgs((m) => [...m, aiMsg]);
    } finally {
      setSending(false);
    }
  }

  function handleKeyDown(e: React.KeyboardEvent<HTMLInputElement>) {
    if (e.key === 'Enter') send();
  }

  return (
    <div className={styles.chat}>
      <div className={styles.history}>
        {msgs.map((m) => (
          <div key={m.id} className={m.role === 'user' ? styles.user : styles.ai}>
            <div className={styles.bubble}>
              <strong>{m.role === 'user' ? 'You' : 'AI'}:</strong> {m.text}
              {m.time && <div className={styles.time}>{m.time}</div>}
            </div>
          </div>
        ))}
        {/* 👇 Dummy div to scroll into view */}
        <div ref={bottomRef} />
      </div>

      <div className={styles.inputRow}>
        <input
          className={styles.input}
          placeholder="Type your message…"
          value={input}
          onChange={(e) => setInput(e.target.value)}
          onKeyDown={handleKeyDown}
        />
        <button className={styles.send} onClick={send} disabled={sending}>
          {sending ? 'Sending…' : 'Send'}
        </button>
      </div>
    </div>
  );
};

export default Chat;
