# 🎮 CodeSyncer Dev Quest

Welcome to **CodeSyncer**, an AI-enhanced full-stack synchronization and collaboration tool. This journey breaks the project down into **7 game-like levels** to help you stay focused, motivated, and always leveling up!

---

## 🧭 Quest Overview

| Level | Name                   | Objective                                 | Due Date        | Status |
|-------|------------------------|-------------------------------------------|------------------|--------|
| 1     | 🌱 The Sync Begins      | Core backend, frontend, and DB setup      | **Aug 4, 2025**  | [X]    |
| 2     | ⚡ Dependency Decoder   | Integrate AI suggestions MVP              | **Aug 9, 2025**  | [X]    |
| 3     | 🧠 Merge Master         | AI-powered diff & merge resolution        | **Aug 18, 2025** | [X]    |
| 4     | 📡 Real-Time Ranger     | Add live collaboration via WebSockets     | **Aug 28, 2025** | [~]    |
| 5     | 🏗️ Architect Mode        | Build advanced UI + dependency tools      | **Sep 6, 2025**  | [ ]    |
| 6     | 🧪 The Tester           | Add tests + analytics for optimization    | **Sep 13, 2025** | [ ]    |
| 7     | 🧬 AI Apprentice (Bonus)| Train your own AI model for suggestions   | **Sep 22, 2025** | [ ]    |


---

## 🌱 **Level 1: The Sync Begins**

🧩 _Goal:_ Set up the foundation for the app.

- [X] Create a Spring Boot backend project with REST API
- [X] Create a Next.js frontend with TypeScript
- [X] Connect PostgreSQL and define base schema (e.g., projects, diffs)
- [X] Create a `/sync` route where frontend sends dummy diff to backend
- [X] Log received data to confirm end-to-end integration

✅ **Reward:** Project skeleton working and connected!

---

## ⚡ **Level 2: Dependency Decoder**

🧩 _Goal:_ Add AI insight system for suggestions.

- [X] Integrate OpenAI or similar SDK
- [X] Build prompt logic to analyze frontend/backend diffs
- [X] Return simple suggestions from AI (e.g. refactor hints)
- [X] Display suggestions in frontend below the submitted diff

✅ **Reward:** Working suggestion engine!

---

## 🧠 **Level 3: Merge Master**

🧩 _Goal:_ Intelligent conflict resolution engine.

- [X] Refactor Code so that it accepts both frontend and backend files
- [X] Compare two code versions and compute diff
- [X] Refactor Code so that there's a sync and diff controller logic separate
- [X] Let AI suggest a merged version of both
- [X] Create accept/reject backend logic
- [X] Display merge suggestion in UI with accept/reject buttons and show success message
- [X] Store accepted merge to DB

✅ **Reward:** Become the master of merging!

---

## 📡 **Level 4: Real-Time Ranger**

🧩 _Goal:_ Enable real-time team collaboration.

- [X] Add WebSocket support to Spring Boot (Spring Web Socket - Not WebFlux)
- [~] Broadcast sync/merge events to connected clients
- [ ] Show active user cursors or edits in real-time
- [ ] Implement basic conflict warning when edits overlap

✅ **Reward:** Real-time dev collab unlocked!

---

## 🏗️ **Level 5: Architect Mode**

🧩 _Goal:_ Build better UI/UX + architecture insights.

- [ ] Show dependency graph of functions/modules
- [ ] Add drag-and-drop file organizer
- [ ] Implement split diff view for review
- [ ] Trigger AI refactor suggestions from UI

✅ **Reward:** Look like a dev god with a powerful UI.

---

## 🧪 **Level 6: The Tester**

🧩 _Goal:_ Ship with quality and speed.

- [ ] Write backend integration/unit tests (Spring Boot)
- [ ] Add frontend tests (Jest + React Testing Lib)
- [ ] Track test coverage + runtime performance
- [ ] Add loading indicators and UI feedback loops

✅ **Reward:** Solid and production-ready.

---

## 🧬 **Level 7: AI Apprentice (Bonus)**

🧩 _Goal:_ Fine-tune your own AI logic.

- [ ] Collect commit histories + merge outcomes
- [ ] Preprocess code for model fine-tuning
- [ ] Fine-tune small transformer or use RAG
- [ ] Use it in place of generic AI APIs for custom merge predictions

✅ **Reward:** You built your own MergeGPT 😤

---

## 🧬 **Level 8: Add Auth**

🧩 _Goal:_ Create authentication logic.

- [ ] Create authentication logic to prevent bot spamming

✅ **Reward:** No bot spamming 😤

## 🏆 Progress Tracker

