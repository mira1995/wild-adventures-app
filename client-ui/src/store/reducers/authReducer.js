import { TOGGLE_AUTH } from '../actions/types'

const initialState = { token: null }

function toggleAuthentication(state = initialState, action) {
  let nextState
  switch (action.type) {
    case TOGGLE_AUTH:
      nextState = {
        ...state,
        token: action.value,
      }
      return nextState || state
    default:
      return state
  }
}

export default toggleAuthentication
