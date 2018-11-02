import { TOGGLE_MENU } from '../actions/types'
import { URI, MENU } from '../../helpers/constants'

const initialState = { current: null }

function toggleMenu(state = initialState, action) {
  let nextState
  switch (action.type) {
    case TOGGLE_MENU:
      let current
      switch (action.value) {
        case URI.HOME:
          current = MENU.HOME
          break
        case URI.CATEGORIES:
          current = MENU.CATEGORIES
          break
        case URI.ADVENTURES:
          current = MENU.ADVENTURES
          break
        case URI.ACCOUNT:
          current = MENU.ACCOUNT
          break
        case URI.REGISTER:
          current = MENU.REGISTER
          break
        case URI.LOGOUT:
          current = MENU.HOME
          break
        case URI.LOGIN:
          current = MENU.LOGIN
          break
        default:
          current = MENU.HOME
      }
      nextState = { ...state, current }
      return nextState || state
    default:
      return state
  }
}

export default toggleMenu
