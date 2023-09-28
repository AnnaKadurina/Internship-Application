import './App.css';
import AppBar from './components/AppBar'
import Footer from './components/Footer'
import { Route, Routes } from "react-router-dom"
import Register from './pages/Register'
import Login from './pages/Login'
import AllProperties from './pages/AllProperties'
import AllBookings from './pages/AllBookings'
import PageNotFound from './components/PageNotFound'
import UserTable from './components/userTable'
import PropertyTable from './components/propertyTable'
import ReviewTable from './components/reviewsTable'
import Settings from './pages/Settings'
import CreateProperty from './pages/CreateProperty'
import CreateReview from './pages/CreateReview'
import CreateUpdateBooking from './pages/CreateUpdateBooking'
import BecomeHostPage from './pages/BecomeHostPage'
import PropertyInfo from './pages/PropertyInfo'
import MyProperties from './pages/MyProperties'
import UpdateProperty from './pages/UpdateProperty'
import ChatRoom from './pages/Chatroom'
import ProtectedRoutes from "./components/ProtectedRoutes"
import Unauthorized from './components/Unauthorized'
import AdminStatistics from './pages/AdminStatistics'
import HostStatistics from './pages/HostStatistics'
import Planning from './pages/Planning'


function App() {
  return (
    <div className="App" style={{ backgroundColor: '#D4DCDE' }}>
      <AppBar />
      <Routes>

        {/* Public routes */}
        <Route path="/" >
          <Route path='/' element={<AllProperties />} />
          <Route path='/register' element={<Register />} />
          <Route path='/login' element={<Login />} />
          <Route path='/home' element={<AllProperties />} />
          <Route path="*" element={<PageNotFound />} />
          <Route path='/settings' element={<Settings />} />
          <Route path='/info/:id' element={<PropertyInfo />} />
          <Route path='/chatroom' element={<ChatRoom />} />
          <Route path="/unauthorized" element={<Unauthorized />} />
        </Route>

        {/* GUEST routes */}
        <Route path="guest" element={<ProtectedRoutes roleRequired="GUEST" />}>
          <Route path='become/host' element={<BecomeHostPage />} />
          <Route path='bookings' element={<AllBookings />} />
          <Route path='create/review/:propertyId' element={<CreateReview />} />
          <Route path='create/booking/:propertyId' element={<CreateUpdateBooking />} />
          <Route path='update/booking/:propertyId' element={<CreateUpdateBooking />} />
        </Route>

        {/* HOST routes */}
        <Route path="host" element={<ProtectedRoutes roleRequired="HOST" />}>
          <Route path='create' element={<CreateProperty />} />
          <Route path='my/properties' element={<MyProperties />} />
          <Route path='update/property/:id' element={<UpdateProperty />} />
          <Route path='planning/:id' element={<Planning />} />
          <Route path="statistics" element={<HostStatistics />} />
        </Route>

        {/* ADMIN routes */}
        <Route path="admin" element={<ProtectedRoutes roleRequired="ADMIN" />}>
          <Route path="overview/users" element={<UserTable />} />
          <Route path="overview/properties" element={<PropertyTable />} />
          <Route path="overview/reviews" element={<ReviewTable />} />
          <Route path="statistics" element={<AdminStatistics />} />
        </Route>

      </Routes>

      <Footer />
    </div>
  );
}

export default App;
