import { createStore } from 'redux'
import toggleAuthentication from './reducers/authReducer'

export default createStore(toggleAuthentication)
