import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faPhone, faEnvelope } from "@fortawesome/free-solid-svg-icons";
import { useState } from "react";

const customerData = {
  contact_name: "John Doe",
  image: "https://randomuser.me/api/portraits/men/45.jpg",
  email: "johndoe@example.com",
  phone: "+1 (555) 123-4567",
};
// const messages = [
//   {
//     id: 1,
//     sender: "John Doe",
//     senderImage: "https://randomuser.me/api/portraits/men/1.jpg",
//     message: "Hey, I just wanted to check in on our project.",
//     timestamp: "2025-04-03 09:00 AM",
//   },
//   {
//     id: 2,
//     sender: "John Doe",
//     senderImage: "https://randomuser.me/api/portraits/men/1.jpg",
//     message: "Have you had a chance to review the latest update?",
//     timestamp: "2025-04-03 09:15 AM",
//   },
//   {
//     id: 3,
//     sender: "John Doe",
//     senderImage: "https://randomuser.me/api/portraits/men/1.jpg",
//     message: "Let me know if you need any changes.",
//     timestamp: "2025-04-03 09:30 AM",
//   },
//   {
//     id: 4,
//     sender: "John Doe",
//     senderImage: "https://randomuser.me/api/portraits/men/1.jpg",
//     message: "Also, do we have a deadline for this?",
//     timestamp: "2025-04-03 09:45 AM",
//   },
//   {
//     id: 5,
//     sender: "John Doe",
//     senderImage: "https://randomuser.me/api/portraits/men/1.jpg",
//     message: "I'll be available all day if you need anything.",
//     timestamp: "2025-04-03 10:00 AM",
//   },
// ];
const users = [
  {
    id: 1,
    name: "John Doe",
    email: "john.doe@example.com",
    phone: "+1 123-456-7890",
    image: "https://randomuser.me/api/portraits/men/1.jpg",
    isOnline: true,
  },
  {
    id: 2,
    name: "Alice Smith",
    email: "alice.smith@example.com",
    phone: "+1 987-654-3210",
    image: "https://randomuser.me/api/portraits/women/2.jpg",
    isOnline: false,
  },
  {
    id: 3,
    name: "Michael Johnson",
    email: "michael.johnson@example.com",
    phone: "+1 555-678-9012",
    image: "https://randomuser.me/api/portraits/men/3.jpg",
    isOnline: true,
  },
  {
    id: 4,
    name: "Emma Brown",
    email: "emma.brown@example.com",
    phone: "+1 444-123-5678",
    image: "https://randomuser.me/api/portraits/women/4.jpg",
    isOnline: false,
  },
  {
    id: 5,
    name: "David Wilson",
    email: "david.wilson@example.com",
    phone: "+1 333-890-2345",
    image: "https://randomuser.me/api/portraits/men/5.jpg",
    isOnline: true,
  },
];
const dummyMessages = [
  {
    id: 1,
    sender: "me",
    content: "Hey Sharon! How’s it going?",
    time: "2:33 am",
    avatar: "https://bootdey.com/img/Content/avatar/avatar1.png",
    name: "You",
  },
  {
    id: 2,
    sender: "them",
    content: "I’m doing well, just getting started on my day!",
    time: "2:34 am",
    avatar: "https://bootdey.com/img/Content/avatar/avatar3.png",
    name: "Sharon Lessman",
  },
  {
    id: 3,
    sender: "me",
    content: "Nice! Got any exciting plans?",
    time: "2:35 am",
    avatar: "https://bootdey.com/img/Content/avatar/avatar1.png",
    name: "You",
  },
  {
    id: 4,
    sender: "them",
    content: "Just working on a new project, I’ll tell you more soon!",
    time: "2:36 am",
    avatar: "https://bootdey.com/img/Content/avatar/avatar3.png",
    name: "Sharon Lessman",
  },
];

export default function NotificationsAdmin() {
  const [filteredUsers, setFilteredUsers] = useState(users);
  const [selectedUser, setSelectedUser] = useState(null);
  const [searchTerm, setSearchTerm] = useState("");
  const [customer, setCustomer] = useState({...customerData});
  const [messages, setMessages] = useState([...dummyMessages]);

  // Handle search form submission
  const searchUsers = (e) => {
    e.preventDefault();
    const searchTerm = e.target.value.toLowerCase();
    const filteredUsers = users.filter((user) =>
      user.name.toLowerCase().includes(searchTerm)
    );
    setFilteredUsers(filteredUsers);
  };

  // Handle user selection
  const handleSelectUser = (user) => {
    setSelectedUser(user);
    // You can add more logic here, e.g. navigate to chat, open modal, etc.
  };
  // Handle search input change
  const handleSearchChange = (e) => {
    setSearchTerm(e.target.value);
  };

  return (
    <main className="content overflow-scroll px-5 py-3 h-100">
      <h1 className="h3 my-3">
        <strong>Chat</strong>
      </h1>
      <div className="container-fluid p-0">
        <div className="row">
          <div className="col-md-3 d-flex">
            <div className="card card-body mb-3">
              <form className="form-inline d-flex mb-3" onSubmit={searchUsers}>
                <input
                  className="form-control mr-sm-2 me-2"
                  type="search"
                  placeholder="Search"
                  aria-label="Search"
                  value={searchTerm}
                  onChange={handleSearchChange}
                />
                <button
                  className="btn btn-outline-success my-2 my-sm-0"
                  type="submit"
                >
                  Search
                </button>
              </form>

              <ul className="list-group list-group-flush h-100 overflow-y-scroll">
                {filteredUsers.map((user) => (
                  <li
                    className={`list-group-item ${
                      selectedUser?.id === user.id ? "active" : ""
                    }`}
                    key={user.id}
                    onClick={() => handleSelectUser(user)}
                    style={{ cursor: "pointer" }}
                  >
                    <div className="d-flex align-items-start">
                      <img
                        src={user.image}
                        className="rounded-circle mr-1"
                        alt={user.name}
                        width="40"
                        height="40"
                      />
                      <div className="flex-grow-1 ml-3 ms-3">
                        <h5 className="mb-2 p-0">{user.name}</h5>

                        <div className="small">
                          <span
                            className={`fas fa-circle ${
                              user.isOnline ? "text-success" : "text-secondary"
                            }`}
                          ></span>{" "}
                          {user.isOnline ? "Online" : "Offline"}
                        </div>
                      </div>
                    </div>
                  </li>
                ))}
              </ul>
            </div>
          </div>
          <div className="col-md-6 d-flex">
            <div className="card card-body mb-3">
              <div className="py-2 px-4 border-bottom d-none d-lg-block">
                <div className="d-flex align-items-center py-1">
                  <div className="position-relative">
                    <img
                      src="https://bootdey.com/img/Content/avatar/avatar3.png"
                      className="rounded-circle mr-1"
                      alt="Sharon Lessman"
                      width="40"
                      height="40"
                    />
                  </div>
                  <div className="ms-2 flex-grow-1 pl-3">
                    <strong>Sharon Lessman</strong>
                    <div className="text-muted small">
                      <em>Typing...</em>
                    </div>
                  </div>
                </div>
              </div>

              <div className="position-relative">
                <div className="chat-messages p-4">
                  {messages.map((msg) => (
                    <div
                      key={msg.id}
                      className={`chat-message-${
                        msg.sender === "me" ? "right" : "left"
                      } pb-4`}
                    >
                      <div>
                        <img
                          src={msg.avatar}
                          className="rounded-circle mr-1"
                          alt={msg.name}
                          width="40"
                          height="40"
                        />
                        <div className="text-muted small text-nowrap mt-2">
                          {msg.time}
                        </div>
                      </div>
                      <div
                        className={`flex-shrink-1 bg-light rounded py-2 px-3 ${
                          msg.sender === "me" ? "mr-3" : "ml-3"
                        }`}
                      >
                        <div className="font-weight-bold mb-1">{msg.name}</div>
                        {msg.content}
                      </div>
                    </div>
                  ))}
                </div>
              </div>

              <div className="flex-grow-0 py-3 px-4 border-top">
                <div className="d-flex align-items-center">
                  <input
                    type="text"
                    className="form-control me-2"
                    placeholder="Type your message"
                  />
                  <button className="btn btn-primary">Send</button>
                </div>
              </div>
            </div>
          </div>
          <div className="col-md-3 d-flex">
            <div className="card card-body mb-3">
              <div className="text-center mb-2">
                <img
                  style={{ borderRadius: "4rem" }}
                  className="mx-auto mb-2"
                  src={customer.image}
                  alt={customer.contact_name}
                />
                <p className="m-0 fs-4">{customer.contact_name}</p>
              </div>
              <hr />
              <div className="d-flex flex-column flex-start px-3">
                <h5>Contact Info</h5>
                <div className="mb-3">
                    <p className="mb-1"><strong>
                        <FontAwesomeIcon icon={faPhone} className="me-2" />
                        Phone:
                    </strong></p>
                  <p>{customer.phone}</p>
                </div>

                <div className="mb-3">
                    <p className="mb-1"><strong>
                        <FontAwesomeIcon icon={faEnvelope} className="me-2" />
                        Email:
                    </strong></p>
                  <p>{customer.email}</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>
  );
}
