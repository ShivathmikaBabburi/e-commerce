import Home from "./Components/Home";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import "./App.css";
import Login from "./Components/Login";
import Demo from "./Components/demo"; // Ensure consistency in casing.
import { action as LoginAction } from "./Components/Login";
import { Loader as HeaderLoader } from "./Components/User";
import { loader as HomeLoader } from "./Components/Home"; // Corrected 'Loader' to 'loader' for consistency.
import Header from "./Components/Header";

const router = createBrowserRouter([
  {
    path: "/",
    element: <Header />,
    loader: HeaderLoader,
    children: [
      {
        index: true,
        loader: HomeLoader,
        element: <Home />,
      },
      {
        path: "dashboard", // Remove the leading slash to make it a relative path.
        element: <Demo />,
        children: [
          {
            path: "demo", // Remove the leading slash to make it relative to /dashboard.
            element: <Demo />,
          },
        ],
      },
    ],
  },
  {
    path: "/login",
    element: <Login />,
    action: LoginAction,
  },
]);

function App() {
  return <RouterProvider router={router} />;
}

export default App;
