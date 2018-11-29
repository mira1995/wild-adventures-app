const initialState = { buyingBox: [] }
function toggleBuyingBox(state = initialState, action) {
  let nextState
  switch (action.type) {
    case 'TOGGLE_BUYINGBOX':
      const sessionIndex = state.buyingBox.findIndex(
        item =>
          item.adventureId === action.value.adventureId &&
          item.startDate === action.value.startDate &&
          item.endDate === action.value.endDate
      )
      if (sessionIndex !== -1) {
        nextState = {
          ...state,
          buyingBox: state.buyingBox.filter(
            (item, index) => index !== sessionIndex
          ),
        }
      } else {
        nextState = {
          ...state,
          buyingBox: [...state.buyingBox, action.value],
        }
      }
      return nextState || state
    case 'ADD_NB_ORDER':
      let buyingBox = state.buyingBox
      buyingBox[action.value.index].nbOrder = action.value.value
      nextState = state
      nextState.buyingBox = buyingBox
      return nextState || state
    default:
      return state
  }
}

export default toggleBuyingBox
