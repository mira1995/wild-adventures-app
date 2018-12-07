import React, { Component } from 'react'
import { Form, Icon, Button, Upload, message } from 'antd'
import moment from 'moment'
import Container from './../../../Container'
import { http } from '../../../configurations/axiosConf'
import { strings } from '../../../helpers/strings'

class ProfileImageForm extends Component {
  constructor() {
    super()
    this.state = { loading: false, imageURI: null }
  }

  componentWillMount() {
    const { user } = this.props
    if (user.profileImageId) {
      http
        .get(`/images/user/${user.profileImageId}`)
        .then(response => this.setState({ imageURI: response.data.uri }))
        .catch(error => console.log(error))
    }
  }

  beforeUpload(file) {
    const isJPG = file.type === 'image/jpeg'
    if (!isJPG) {
      message.error('You can only upload JPG file!')
    }
    const isLt2M = file.size / 1024 / 1024 < 1
    if (!isLt2M) {
      message.error('Image must smaller than 1MB!')
    }
    return isJPG && isLt2M
  }

  handleChange = info => {
    if (info.file.status === 'uploading') {
      this.setState({ loading: true })
    }
    if (info.file.status === 'done') {
      this.setState({ loading: false })
    }
  }

  handleUpload = upload => {
    console.log('fileObjectRequest', upload)
    const { user } = this.props

    const profileImageName = `${moment().format('YYYYMMDDhhmmss')}-profile.jpg`
    const profileImage = {
      alt: 'user profile',
      description: 'User picture',
      uri: `/users/${user.id}/${profileImageName}`,
      type: {
        code: 'USR',
        name: 'user',
      },
    }

    const data = new FormData()
    data.append('avatar', upload.file)
    data.append('userID', user.id)
    data.append('filename', profileImageName)
    const config = {
      headers: {
        'content-type':
          'multipart/form-data; boundary=----WebKitFormBoundaryyrV7KO0BoCBuDbTL',
      },
    }

    if (user.profileImageId) {
      http
        .patch('/images/user', { ...profileImage, id: user.profileImageId })
        .then(response => {
          this.setState({ imageURI: response.data.uri })
          http
            .post('http://localhost:8000/avatar', data, config)
            .then(() => upload.onSuccess('done'))
            .catch(error => console.log(error))
          upload.onSuccess('done')
        })
        .catch(error => console.log('error', error))
    } else {
      http
        .post('/images/user', profileImage)
        .then(response => {
          this.props.action({ profileImageId: response.data.id })
          this.setState({ imageURI: response.data.uri })
          http
            .post('http://localhost:8000/avatar', data, config)
            .then(() => upload.onSuccess('done'))
            .catch(error => console.log(error))
          upload.onSuccess('done')
        })
        .catch(error => console.log('error', error))
    }
  }

  render() {
    const FormItem = Form.Item
    const { getFieldDecorator } = this.props.form

    const formItemLayout = {
      labelCol: {
        xs: { span: 24 },
        sm: { span: 8 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
    }

    const { user } = this.props
    const { imageURI } = this.state

    return (
      <Container>
        <Form onSubmit={this.handleSubmit}>
          <FormItem {...formItemLayout} label={strings.user.form.avatarLabel}>
            {getFieldDecorator('profileImage', {})(
              <Upload
                name="avatar"
                listType="picture-card"
                showUploadList={false}
                customRequest={this.handleUpload}
                beforeUpload={this.beforeUpload}
                onChange={this.handleChange}
              >
                {user.profileImageId && imageURI ? (
                  <div
                    style={{
                      backgroundImage: `url(http://localhost:8000/images${imageURI})`,
                      backgroundSize: 'cover',
                      backgroundPosition: 'center center',
                      width: '100%',
                      height: '100%',
                    }}
                  />
                ) : (
                  <Button>
                    <Icon type={this.state.loading ? 'loading' : 'plus'} />
                    {strings.user.upload}
                  </Button>
                )}
              </Upload>
            )}
          </FormItem>
        </Form>
      </Container>
    )
  }
}

const WrappedProfileImageForm = Form.create()(ProfileImageForm)

export default WrappedProfileImageForm
