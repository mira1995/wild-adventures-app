import express from 'express'
import bodyParser from 'body-parser'
import cors from 'cors'
import fileUpload from 'express-fileupload'
import fs from 'fs-extra'

const app = express()
app.use(cors())
app.use(fileUpload())
app.use(express.static(__dirname + '/public'))

// Parse incoming requests data
app.use(bodyParser.json())
app.use(bodyParser.urlencoded({ extended: true }))

const PORT = 8000

app.get('/', function(req, res) {
  res.send('hello world')
})

app.post('/avatar', (req, res) => {
  let imageFile = req.files.avatar
  const folder = `${__dirname}/public/images/users/${req.body.userID}`

  if (!fs.existsSync(folder)) {
    fs.mkdirSync(folder)
  } else {
    fs.removeSync(folder)
    fs.mkdirSync(folder)
  }

  imageFile.mv(`${folder}/${req.body.filename}`, err => {
    if (err) return res.status(500).send(err)
    res.json({ file: `${folder}/${req.body.filename}` })
  })
})

app.delete('/avatar', (req, res) => {
  const folder = `${__dirname}/public/images/users/${req.body.userID}`
  if (fs.existsSync(folder)) fs.removeSync(folder)
  res.send('Folder successfully deleted.')
})

app.listen(PORT, () => {
  console.log(`Server running on port ${PORT}`)
})
