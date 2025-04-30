let timerInterval;
let minutes = 0;
let seconds = 0;
let laps = 0;
let streaks = 0;
let activeMode = null; // Current mode ("pomodoro", "timeBoxing", "feynman") or null
let isTimerRunning = false; // Indicates if the timer is running
let userId = null; // Store the current user's ID

// Updates the timer display
function updateTimerDisplay() {
    const minutesElement = document.getElementById("minutes");
    const secondsElement = document.getElementById("seconds");

    if (minutesElement && secondsElement) {
        minutesElement.textContent = String(minutes).padStart(2, "0");
        secondsElement.textContent = String(seconds).padStart(2, "0");
    }
}

// Generates a unique session ID
function generateSessionId() {
    return `session-${Date.now()}`;
}

// Toggles the timer between running and paused
function toggleTimer() {
    const startIcon = document.getElementById("start-icon");
    if (isTimerRunning) {
        clearInterval(timerInterval);
        isTimerRunning = false;
        startIcon.className = "fas fa-play";
    } else {
        if (activeMode) {
            startDecrementingTimer();
        } else {
            startIncrementingTimer();
        }
        isTimerRunning = true;
        startIcon.className = "fas fa-pause";
    }
}

// Starts an incrementing timer (normal mode)
function startIncrementingTimer() {
    clearInterval(timerInterval);
    timerInterval = setInterval(() => {
        seconds++;
        if (seconds === 60) {
            minutes++;
            seconds = 0;
        }
        updateTimerDisplay();
    }, 1000);
}

// Starts a decrementing timer (for modes)
function startDecrementingTimer() {
    clearInterval(timerInterval);
    timerInterval = setInterval(() => {
        if (minutes === 0 && seconds === 0) {
            stopTimer();
            return;
        }
        if (seconds === 0) {
            minutes--;
            seconds = 59;
        } else {
            seconds--;
        }
        updateTimerDisplay();
    }, 1000);
}

// Starts a new session and retrieves the user ID
async function startSession() {
    try {
        const response = await fetch("/session/start", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                'Csrf-Token': csrfToken  // Include the CSRF token here
            },
        });

        if (response.ok) {
            const data = await response.json();
            userId = data.userId; // Store the userId
            console.log(`New session started with userId: ${userId}`);
        } else {
            console.error("Failed to start session");
        }
    } catch (error) {
        console.error("Error starting session:", error);
    }
}


// Stops the timer, saves the session, and resets
async function stopTimer() {
    if (!isTimerRunning) return;

    clearInterval(timerInterval);
    isTimerRunning = false;

    // Ensure userId is available; generate one if missing
    if (!userId) {
        console.warn("User ID is missing. Generating a new user ID.");
        userId = generateUserId(); // Generate a new user ID
        document.cookie = `userId=${userId}; path=/;`;
    }

    // Calculate duration
    let duration = minutes * 60 + seconds; // Total elapsed time in seconds

    // Handle modes (Pomodoro, Time Boxing, Feynman)
    if (activeMode) {
        const totalSessionTime = activeMode === "pomodoro" ? 25 * 60 :
                                 activeMode === "timeBoxing" ? parseInt(document.getElementById("timeInput")?.value || 30) * 60 :
                                 activeMode === "feynman" ? 45 * 60 : 0;
        duration = totalSessionTime - duration; // Elapsed time for modes
    }

    // Prepare session data
    const sessionData = {
        sessionId: generateSessionId(),
        userId: userId, // Use the ensured userId
        startTime: new Date().toISOString(),
        endTime: new Date().toISOString(),
        duration: duration, // Save elapsed time
        laps: laps,
        streaks: streaks,
    };

    // Save session to database
    try {
        const csrfToken = document.querySelector("meta[name='csrfToken']").getAttribute("content");
        const response = await fetch("/session/stop", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Csrf-Token": csrfToken,
            },
            body: JSON.stringify(sessionData),
        });

        if (!response.ok) {
            const errorDetails = await response.json();
            throw new Error(`Failed to save session data: ${errorDetails.message}`);
        }

        console.log("Session saved successfully!");
    } catch (error) {
        console.error("Error while saving session data:", error);
    }

    // Fetch updated sessions and reset timer
    fetchUserSessions();
    resetTimer();
    activeMode = null; // Clear active mode
}

// Generates a random user ID
function generateUserId() {
    return `user-${Math.random().toString(36).substr(2, 9)}`;
}

// Function to toggle the session history box and the icon
function toggleSessionHistory() {
    const sessionHistoryBox = document.querySelector('.session-history-container');
    const isExpanded = sessionHistoryBox.classList.contains('expanded');
    const toggleButtonIcon = document.querySelector('.toggle-session-button i'); // Get the icon element

    // Toggle the expanded class to expand/collapse the box
    if (isExpanded) {
        sessionHistoryBox.classList.remove('expanded');
        toggleButtonIcon.classList.remove('fa-angle-double-up'); // Remove the up arrow icon
        toggleButtonIcon.classList.add('fa-angle-double-down'); // Add the down arrow icon
    } else {
        sessionHistoryBox.classList.add('expanded');
        toggleButtonIcon.classList.remove('fa-angle-double-down'); // Remove the down arrow icon
        toggleButtonIcon.classList.add('fa-angle-double-up'); // Add the up arrow icon
    }
}
// Fetches user-specific sessions and displays them
// Fetches user-specific sessions
async function fetchUserSessions() {
    if (!userId) {
        console.error("No userId available to fetch sessions");
        return;
    }

    try {
        const response = await fetch(`/sessions/${userId}`);
        const sessions = await response.json();
        displayUserSessions(sessions); // This function will handle the rendering
    } catch (error) {
        console.error("Error fetching user sessions:", error);
    }
}

// Updates the session history UI and places the newest session at the top
function displayUserSessions(sessions) {
    const sessionHistoryList = document.getElementById("session-history-list");
    sessionHistoryList.innerHTML = ""; // Clear existing content

    // Display sessions in the order of most recent (newest first)
    sessions.forEach(session => {
        const sessionItem = document.createElement("li");
        sessionItem.innerHTML = `
            <div><strong>Start Time:</strong> ${formatDateTime(session.startTime)}</div>
            <div><strong>End Time:</strong> ${session.endTime ? formatDateTime(session.endTime) : "Ongoing"}</div>
            <div><strong>Duration:</strong> ${session.duration} seconds</div>
            <div><strong>Laps:</strong> ${session.laps}</div>
            <div><strong>Streaks:</strong> ${session.streaks}</div>
        `;
        sessionHistoryList.prepend(sessionItem); // Prepend to make it appear at the top
    });
}

// Resets the timer and clears laps
function resetTimer() {
    clearInterval(timerInterval);
    minutes = 0;
    seconds = 0;
    laps = 0;
    streaks = 0;
    isTimerRunning = false;
    updateTimerDisplay();
    clearLapsDisplay();
    document.getElementById("start-icon").className = "fas fa-play";
}

// Adds a lap and displays it
function addLap() {
    if (!isTimerRunning) return;

    laps++;
    if (laps % 5 === 0) streaks++;

    const lapsList = document.getElementById("laps-streaks-list");
    const lapBox = document.createElement("div");
    lapBox.textContent = `Lap ${laps}`;
    lapBox.classList.add("lap-box");
    lapsList.appendChild(lapBox);

    if (laps % 5 === 0) {
        const streakBox = document.createElement("div");
        streakBox.textContent = `Streak ${streaks}`;
        streakBox.classList.add("streak-box");
        lapsList.appendChild(streakBox);
    }
}

// Clears laps and streaks
function clearLapsDisplay() {
    const lapsList = document.getElementById("laps-streaks-list");
    lapsList.innerHTML = "";
    laps = 0;
    streaks = 0;
}

// Clears session details
function clearSessionDetails() {
    const sessionDetails = document.getElementById("session-details");
    sessionDetails.innerHTML = "";
}

// Starts Pomodoro mode
function startPomodoro() {
    resetTimer();
    activeMode = "pomodoro";
    minutes = 25;
    seconds = 0;
    updateTimerDisplay();
    toggleTimer();
}

// Prompts the user to set time for Time Boxing mode
function startTimeBoxing() {
    resetTimer();
    activeMode = "timeBoxing";

    const modal = document.createElement("div");
    modal.id = "time-boxing-modal";
    modal.style.cssText = `
        position: fixed;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
        background: white;
        padding: 20px;
        border-radius: 10px;
        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
        z-index: 1000;
    `;

    modal.innerHTML = `
        <h3>Set Time for Time Boxing</h3>
        <input type="number" id="timeInput" min="1" max="120" value="30" style="width: 100%; padding: 10px;">
        <div style="margin-top: 20px; display: flex; justify-content: space-around;">
            <button onclick="setTimeBoxing()" style="padding: 10px 20px; background-color: green; color: white; border: none; border-radius: 5px;">OK</button>
            <button onclick="cancelTimeBoxing()" style="padding: 10px 20px; background-color: red; color: white; border: none; border-radius: 5px;">Cancel</button>
        </div>
    `;

    document.body.appendChild(modal);
    document.getElementById("timeInput").focus();
}

// Sets the timer for Time Boxing mode
function setTimeBoxing() {
    const timeInput = document.getElementById("timeInput").value;
    if (!timeInput || isNaN(timeInput) || timeInput <= 0 || timeInput > 120) {
        alert("Please enter a valid time between 1 and 120 minutes.");
        return;
    }

    minutes = parseInt(timeInput, 10);
    seconds = 0;
    updateTimerDisplay();
    toggleTimer();

    const modal = document.getElementById("time-boxing-modal");
    if (modal) modal.remove();
}

// Cancels the Time Boxing setup
function cancelTimeBoxing() {
    const modal = document.getElementById("time-boxing-modal");
    if (modal) modal.remove();
    activeMode = null;
}

// Starts Feynman mode with breaks every 15 minutes
function startFeynman() {
    resetTimer();
    activeMode = "feynman";
    minutes = 45;
    seconds = 0;
    updateTimerDisplay();

    let pauseCounter = 0; // Track the number of pauses

    timerInterval = setInterval(() => {
        // Check if the timer has completed
        if (minutes === 0 && seconds === 0) {
            clearInterval(timerInterval);
            stopTimer();
            return;
        }

        // Trigger a pause every 15 minutes
        if (minutes % 15 === 0 && seconds === 0 && pauseCounter < 3) {
            clearInterval(timerInterval); // Pause the timer
            pauseCounter++;
            
 // Show the custom modal
 showBreakWarningModal(() => {
    startDecrementingTimer(); // Resume decrementing the timer after modal is closed
});

            // Resume the timer after 10 seconds
            setTimeout(() => {
                startDecrementingTimer(); // Continue decrementing the timer
            }, 10000);

            return; // Exit to prevent further decrements
        }

        // Regular timer decrement logic
        if (seconds === 0) {
            minutes--;
            seconds = 59;
        } else {
            seconds--;
        }

        updateTimerDisplay();
    }, 1000);
}


// Show the break warning modal
function showBreakWarningModal() {
    const modal = document.getElementById("break-warning-modal");
    modal.style.display = "flex";

    // Close the modal when the "OK" button is clicked
    const closeModalButton = document.getElementById("close-warning-modal");
    closeModalButton.onclick = () => {
        modal.style.display = "none";
    };
}


// Helper to format ISO date to readable format
function formatDateTime(isoString) {
    const date = new Date(isoString);
    return date.toLocaleString("en-US", {
        year: "numeric",
        month: "long",
        day: "numeric",
        hour: "2-digit",
        minute: "2-digit",
        second: "2-digit",
    });
}
