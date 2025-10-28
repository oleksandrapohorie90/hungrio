import { useState } from "react";
import api from "../utils/api";

type Msg = { role: "user" | "ai"; text: string; time?: string };

export default function Chat() {
  const [input, setInput] = useState("");
  const [msgs, setMsgs] = useState<Msg[]>([]);

  async function send() {
    if (!input.trim()) return;
    const userText = input.trim();
    setInput("");
    setMsgs((m) => [...m, { role: "user", text: userText }]);

    try {
      const { data } = await api.post("/chat", { message: userText });
      // backend DTO: { response, timestamp }
      setMsgs((m) => [...m, { role: "ai", text: data.response, time: data.timestamp }]);
    } catch (e: any) {
      setMsgs((m) => [...m, { role: "ai", text: `Error: ${e?.response?.status ?? ""}` }]);
    }
  }

  return (
    <div style={{ maxWidth: 640, margin: "2rem auto" }}>
      <h2>AI Chat</h2>
      <div style={{ border: "1px solid #ddd", height: 420, padding: 12, overflowY: "auto" }}>
        {msgs.map((m, i) => (
          <div key={i} style={{ textAlign: m.role === "user" ? "right" : "left", margin: "8px 0" }}>
            <div><strong>{m.role === "user" ? "You" : "AI"}:</strong> {m.text}</div>
            {m.time && <small>{m.time}</small>}
          </div>
        ))}
      </div>
      <div style={{ display: "flex", gap: 8, marginTop: 8 }}>
        <input
          value={input}
          onChange={(e) => setInput(e.target.value)}
          placeholder="Type a message…"
          style={{ flex: 1 }}
        />
        <button onClick={send}>Send</button>
      </div>
    </div>
  );
}
