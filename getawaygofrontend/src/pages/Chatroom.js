import React, { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';
import { over } from 'stompjs';
import SockJS from 'sockjs-client';
import '../styles/chat.css';
import { getUser } from '../api/authorizationApis';
import Avatar from '@mui/material/Avatar';
import { loadMessages } from '../api/messageApis';

var stompClient = null;

const ChatRoom = () => {
    const location = useLocation();
    const receiverUsername = location.state?.receiverUsername || '';
    const receiverPhoto = location.state?.receiverPhoto || '';
    const [privateChats, setPrivateChats] = useState(new Map());
    const [tab, setTab] = useState(receiverUsername || "CHATROOM");
    const [userData, setUserData] = useState({
        receiverPhoto: '',
        username: '',
        receiverUsername: receiverUsername,
        connected: false,
        message: ''
    });
    const [data, setData] = useState({});

    useEffect(() => {
        getUser(localStorage.getItem('id'))
            .then(async (response) => {
                userData.username = response.data.username;
                registerUser();
                setData(response.data);
                if (userData.receiverUsername && !privateChats.has(userData.receiverUsername)) {
                    privateChats.set(userData.receiverUsername, []);
                    setPrivateChats(new Map(privateChats));
                    setTab(userData.receiverUsername); // Set the tab to the receiverUsername
                }
            })
            .catch((e) => {
                // Handle error
            });
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    const registerUser = () => {
        connect();
    };

    const connect = () => {
        let Sock = new SockJS('http://localhost:8090/ws');
        stompClient = over(Sock);
        stompClient.connect({}, onConnected, onError);
    };

    const onConnected = () => {
        setUserData({ ...userData, "connected": true });
        stompClient.subscribe('/user/' + userData.username + '/private', onPrivateMessage);
        userJoin();
    };

    const userJoin = () => {
        var chatMessage = {
            senderUsername: userData.username,
            status: "JOIN"
        };
        stompClient.send("/app/message", {}, JSON.stringify(chatMessage));
        loadMessages(userData.username)
            .then((response) => {
                // Handle messages loading
            })
            .catch((e) => {
                // Handle error
            });
    };

    const onPrivateMessage = (payload) => {
        var payloadData = JSON.parse(payload.body);
        if (privateChats.get(payloadData.senderUsername)) {
            privateChats.get(payloadData.senderUsername).push(payloadData);
            if (payloadData.senderUsername === userData.username) {
                var chatMessage = {
                    senderUsername: userData.username,
                    receiverUsername: payloadData.receiverUsername,
                    message: payloadData.message,
                    status: "MESSAGE"
                };
                if (!privateChats.has(payloadData.receiverUsername)) {
                    privateChats.set(payloadData.receiverUsername, []);
                }
                privateChats.get(payloadData.receiverUsername).push(chatMessage);
                setPrivateChats(new Map(privateChats));
            }
            setPrivateChats(new Map(privateChats));

        } else {
            if (payloadData.senderUsername === userData.username) {
                var chatMessage2 = {
                    senderUsername: userData.username,
                    receiverUsername: payloadData.receiverUsername,
                    message: payloadData.message,
                    status: "MESSAGE"
                };
                if (!privateChats.has(payloadData.receiverUsername)) {
                    privateChats.set(payloadData.receiverUsername, []);
                }
                privateChats.get(payloadData.receiverUsername).push(chatMessage2);
                setPrivateChats(new Map(privateChats));
            }
            setPrivateChats(new Map(privateChats));

        }
        privateChats.forEach((messages) => {
            messages.sort((a, b) => new Date(a.date) - new Date(b.date));
        });
    };


    const onError = (err) => {
        console.log(err);
    };

    const handleMessage = (event) => {
        const { value } = event.target;
        setUserData({ ...userData, "message": value });
    };

    const sendPrivateValue = () => {
        if (stompClient) {
            const chatMessage = {
                senderUsername: userData.username,
                receiverUsername: tab,
                message: userData.message,
                status: "MESSAGE"
            };
            console.log(chatMessage);
            if (userData.username !== tab) {
                privateChats.get(tab).push(chatMessage);
                setPrivateChats(new Map(privateChats));
            }
            stompClient.send("/app/private-message", {}, JSON.stringify(chatMessage));
            setUserData({ ...userData, message: "" });
        }
    };

    return (
        <div className="container">
            {data && userData.connected ? (
                <div className="chat-box">
                    <div className="member-list">
                        <ul>
                            {[...privateChats.keys()].map((name, index) => (
                                <li
                                    onClick={() => {
                                        setTab(name);
                                    }}
                                    className={`member ${tab === name && "active"}`}
                                    key={index}
                                >
                                    {name}
                                </li>
                            ))}
                        </ul>
                    </div>
                    {tab !== "CHATROOM" && (
                        <div className="chat-content">
                            <ul className="chat-messages" style={{ overflowY: 'scroll' }}>
                                {[...privateChats.get(tab)].map((chat, index) => (
                                    <li
                                        className={`message ${chat.senderUsername === userData.username && "self"
                                            }`}
                                        key={index}
                                    >
                                        {chat.senderUsername !== userData.username && (
                                            <div>
                                                <Avatar
                                                    alt="Profile picture"
                                                    src={receiverPhoto}
                                                    sx={{ width: 50, height: 50 }}
                                                />
                                            </div>
                                        )}
                                        <div className="message-data">{chat.message}</div>
                                        {chat.senderUsername === userData.username && (
                                            <div>
                                                <Avatar
                                                    alt="Profile picture"
                                                    src={data.photo}
                                                    sx={{ width: 50, height: 50 }}
                                                />
                                            </div>
                                        )}
                                    </li>
                                ))}
                            </ul>

                            <div className="send-message">
                                <input
                                    type="text"
                                    className="input-message"
                                    placeholder="enter the message"
                                    value={userData.message}
                                    onChange={handleMessage}
                                />
                                <button
                                    type="button"
                                    className="send-button"
                                    onClick={sendPrivateValue}
                                >
                                    send
                                </button>
                            </div>
                        </div>
                    )}
                </div>
            ) : null}
        </div>
    );
};

export default ChatRoom;
