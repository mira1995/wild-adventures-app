export const BEARER_TOKEN = 'bearerToken'
export const MENU = {
  HOME: 'home',
  CATEGORIES: 'categories',
  ADVENTURES: 'adventures',
  ACCOUNT: 'account',
  REGISTER: 'register',
  LOGOUT: 'logout',
  LOGIN: 'login',
  MYORDERS: 'Mes commandes',
}
export const URI = {
  HOME: '/',
  CATEGORIES: '/categories',
  ADVENTURES: '/adventures',
  ACCOUNT: '/account',
  REGISTER: '/register',
  LOGOUT: '/logout',
  LOGIN: '/login',
  FORGOT_PASSWORD: '/forgot',
  ORDER: '/order',
  PAYMENT: '/payment',
  MYORDERS: '/myOrders',
  CANCELDEMAND: '/cancel',
  UPDATEDEMAND: '/update',
}
export const API = {
  BASE_URL: 'http://localhost:9000',
  AUTH: '/auth',
  CATEGORIES: '/categories',
  ADVENTURES: '/adventures',
  SESSIONS: '/adventures/sessions',
  COMMENTS: '/comments',
  USERS: '/users',
  ORDERS: '/orders',
  DEMANDS: '/demands',
}
export const CONF = {
  CARD_CONTENT_SIZE: 75,
}

export const ORDERSTATUS = {
  FINALIZED: 'FINALIZED',
  NOT_PAID: 'NOT_PAID',
  UPDATE_DEMAND: 'UPDATE_DEMAND',
  DELETE_DEMAND: 'DELETE_DEMAND',
  CANCELED: 'CANCELED',
}

export const DEMANDSTATUS = {
  OPENED_DEMAND: 'OPENED_DEMAND',
  VALIDATED_DEMAND: 'VALIDATED_DEMAND',
  REJECTED_DEMAND: 'REJECTED_DEMAND',
}
