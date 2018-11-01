import { combineReducers, createStore } from 'redux'
import toggleAuthentication from './reducers/authReducer'
import toggleMenu from './reducers/menuReducer'

const rootReducer = combineReducers({
  authentication: toggleAuthentication,
  menu: toggleMenu,
})

export default createStore(rootReducer)
