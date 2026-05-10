import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import { ProtectedRoute } from "../auth/ProtectedRoute";

import MainLayout from "../layouts/MainLayout";
import CustomerLayout from "../layouts/CustomerLayout";
import AdminLayout from "../layouts/AdminLayout";

import HomePage from "../pages/public/HomePage";
import LoginPage from "../pages/public/LoginPage";
import RegisterPage from "../pages/public/RegisterPage";

import CustomerDashboardPage from "../pages/customer/CustomerDashboardPage";
import SearchJourneyPage from "../pages/customer/SearchJourneyPage";
import BookingPage from "../pages/customer/BookingPage";
import BookingConfirmationPage from "../pages/customer/BookingConfirmationPage";
import MyBookingsPage from "../pages/customer/MyBookingsPage";
import AdminDashboardPage from "../pages/admin/AdminDashboardPage";

import ForbiddenPage from "../pages/errors/ForbiddenPage";
import NotFoundPage from "../pages/errors/NotFoundPage";

import AdminStationsPage from "../pages/admin/AdminStationsPage";
import AdminTrainsPage from "../pages/admin/AdminTrainsPage";
import AdminRoutesPage from "../pages/admin/AdminRoutesPage";
import AdminTrainRunsPage from "../pages/admin/AdminTrainRunsPage";
import AdminDelaysPage from "../pages/admin/AdminDelaysPage";
import AdminEmailsPage from "../pages/admin/AdminEmailsPage";
import AdminBookingsPage from "../pages/admin/AdminBookingsPage";

export default function AppRouter() {
  return (
    <BrowserRouter>
      <Routes>
        <Route element={<MainLayout />}>
          <Route path="/" element={<HomePage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />
          <Route path="/forbidden" element={<ForbiddenPage />} />
        </Route>

        <Route
          path="/customer"
          element={
            <ProtectedRoute allowedRoles={["CUSTOMER"]}>
              <CustomerLayout />
            </ProtectedRoute>
          }
        >
          <Route index element={<CustomerDashboardPage />} />
          <Route path="search" element={<SearchJourneyPage />} />
          <Route path="book" element={<BookingPage />} />
          <Route path="booking-confirmation" element={<BookingConfirmationPage />}/>
          <Route path="bookings" element={<MyBookingsPage />} />
        </Route>

        <Route
          path="/admin"
          element={
            <ProtectedRoute allowedRoles={["ADMIN"]}>
              <AdminLayout />
            </ProtectedRoute>
          }
        >
          <Route index element={<AdminDashboardPage />} />
          <Route path="stations" element={<AdminStationsPage />} />
          <Route path="trains" element={<AdminTrainsPage />} />
          <Route path="routes" element={<AdminRoutesPage />} />
          <Route path="train-runs" element={<AdminTrainRunsPage />} />
          <Route path="delays" element={<AdminDelaysPage />} />
          <Route path="emails" element={<AdminEmailsPage />} />
          <Route path="bookings" element={<AdminBookingsPage />} />
        </Route>

        <Route path="/404" element={<NotFoundPage />} />
        <Route path="*" element={<Navigate to="/404" replace />} />
      </Routes>
    </BrowserRouter>
  );
}