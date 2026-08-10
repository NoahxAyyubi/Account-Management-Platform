const path = require("path");
const express = require("express");

const app = express();
const port = Number(process.env.PORT || 3000);

app.use(express.json());
app.use(express.static(path.join(__dirname, "..", "public")));

const demoUser = {
  email: "qa.user@demo.com",
  password: "Password123!",
  name: "Demo Customer",
  token: "demo-ci-token",
};

const seedSubscriptions = [
  {
    id: 1001,
    customerName: "Demo Customer",
    plan: "Essentials",
    status: "ACTIVE",
    monthlyPrice: 60.0,
  },
  {
    id: 1002,
    customerName: "Family Account",
    plan: "Magenta",
    status: "ACTIVE",
    monthlyPrice: 80.0,
  },
  {
    id: 1003,
    customerName: "Suspended Account",
    plan: "Essentials",
    status: "SUSPENDED",
    monthlyPrice: 45.0,
  },
];

const subscriptions = new Map(seedSubscriptions.map((subscription) => [
  subscription.id,
  { ...subscription },
]));

function resetSubscriptions() {
  subscriptions.clear();
  seedSubscriptions.forEach((subscription) => {
    subscriptions.set(subscription.id, { ...subscription });
  });
}

function getSubscription(id) {
  return subscriptions.get(Number(id));
}

app.get("/api/health", (_req, res) => {
  res.json({
    app: "T-Mobile Demo Account Portal",
    status: "OK",
  });
});

app.post("/api/auth/login", (req, res) => {
  const { email, password } = req.body;

  if (email === demoUser.email && password === demoUser.password) {
    return res.status(200).json({
      authenticated: true,
      token: demoUser.token,
      user: {
        email: demoUser.email,
        name: demoUser.name,
      },
    });
  }

  return res.status(401).json({
    authenticated: false,
    message: "Invalid email or password",
  });
});

app.get("/api/subscriptions", (_req, res) => {
  res.json(Array.from(subscriptions.values()));
});

app.post("/api/test/reset", (_req, res) => {
  resetSubscriptions();
  res.status(200).json({
    message: "Demo data reset",
  });
});

app.get("/api/subscriptions/:id", (req, res) => {
  const subscription = getSubscription(req.params.id);

  if (!subscription) {
    return res.status(404).json({
      message: "Subscription not found",
      id: Number(req.params.id),
    });
  }

  return res.status(200).json(subscription);
});

app.put("/api/subscriptions/:id/plan", (req, res) => {
  const subscription = getSubscription(req.params.id);
  const { plan } = req.body;

  if (!subscription) {
    return res.status(404).json({
      message: "Subscription not found",
      id: Number(req.params.id),
    });
  }

  if (!["Essentials", "Magenta", "Premium"].includes(plan)) {
    return res.status(400).json({
      message: "Plan is not available",
      plan,
    });
  }

  subscription.plan = plan;
  subscription.monthlyPrice = plan === "Premium" ? 95.0 : plan === "Magenta" ? 80.0 : 60.0;

  return res.status(200).json({
    message: "Plan updated successfully",
    subscription,
  });
});

app.listen(port, () => {
  console.log(`T-Mobile Demo Account Portal is running at http://localhost:${port}`);
});
