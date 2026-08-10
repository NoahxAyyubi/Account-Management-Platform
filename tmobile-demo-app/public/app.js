const output = document.querySelector("#output");
const tabs = document.querySelectorAll(".tab");
const panels = document.querySelectorAll(".panel");
const loginForm = document.querySelector("#loginForm");
const loginError = document.querySelector("#loginError");
const dashboard = document.querySelector("#dashboard");
const subscriptionTableBody = document.querySelector("#subscriptionTable tbody");
const currentPlan = document.querySelector("#currentPlan");
const planSelect = document.querySelector("#planSelect");
const planMessage = document.querySelector("#planMessage");
const changePlanButton = document.querySelector("#changePlanButton");
const healthCheckButton = document.querySelector("#healthCheck");
const clearOutputButton = document.querySelector("#clearOutput");

function showResult(title, payload) {
  output.textContent = `${title}\n\n${JSON.stringify(payload, null, 2)}`;
}

function showTab(tabName) {
  tabs.forEach((tab) => tab.classList.toggle("active", tab.dataset.tab === tabName));
  panels.forEach((panel) => panel.classList.toggle("active", panel.id === `${tabName}-panel`));
}

async function readJson(response) {
  return {
    httpStatus: response.status,
    ok: response.ok,
    body: await response.json(),
  };
}

function renderSubscriptions(subscriptions) {
  subscriptionTableBody.innerHTML = "";

  subscriptions.forEach((subscription) => {
    const row = document.createElement("tr");
    row.dataset.subscriptionId = String(subscription.id);
    row.innerHTML = `
      <td>${subscription.id}</td>
      <td>${subscription.customerName}</td>
      <td data-column="plan">${subscription.plan}</td>
      <td>${subscription.status}</td>
      <td>$${subscription.monthlyPrice.toFixed(2)}</td>
    `;
    subscriptionTableBody.appendChild(row);
  });

  const primarySubscription = subscriptions.find((subscription) => subscription.id === 1001);
  if (primarySubscription) {
    currentPlan.textContent = primarySubscription.plan;
    planSelect.value = primarySubscription.plan;
  }
}

async function loadSubscriptions() {
  const response = await fetch("/api/subscriptions");
  const subscriptions = await response.json();
  renderSubscriptions(subscriptions);
}

loginForm.addEventListener("submit", async (event) => {
  event.preventDefault();
  loginError.textContent = "";

  const response = await fetch("/api/auth/login", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
      email: document.querySelector("#email").value,
      password: document.querySelector("#password").value,
    }),
  });

  const result = await readJson(response);
  showResult("Login Response", result);

  if (!response.ok) {
    loginError.textContent = result.body.message;
    dashboard.hidden = true;
    return;
  }

  dashboard.hidden = false;
  await loadSubscriptions();
});

changePlanButton.addEventListener("click", async () => {
  const response = await fetch("/api/subscriptions/1001/plan", {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ plan: planSelect.value }),
  });

  const result = await readJson(response);
  showResult("Plan Change Response", result);

  if (response.ok) {
    planMessage.textContent = "Plan updated successfully";
    await loadSubscriptions();
  } else {
    planMessage.textContent = result.body.message;
  }
});

healthCheckButton.addEventListener("click", async () => {
  const response = await fetch("/api/health");
  showResult("Health Check", await readJson(response));
});

clearOutputButton.addEventListener("click", () => {
  output.textContent = "No action has run yet.";
});

tabs.forEach((tab) => {
  tab.addEventListener("click", () => showTab(tab.dataset.tab));
});
