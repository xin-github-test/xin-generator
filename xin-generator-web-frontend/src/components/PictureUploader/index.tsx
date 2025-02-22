import { COS_HOST } from '@/constants';
import { testDownloadFileUsingGet, testUploadFileUsingPost, uploadFileUsingPost } from '@/services/backend/fileController';
import { InboxOutlined, LoadingOutlined, PlusOutlined } from '@ant-design/icons';
import { Button, Card, Flex, message, Upload, UploadProps, Divider } from 'antd';
import { UploadFile } from 'antd/lib';
import { saveAs } from 'file-saver';
import React, { useState } from 'react';

const { Dragger } = Upload;

interface Props {
  biz: string;
  onChange?: (url: string) => void;
  value?: string;
}
/**
 * 文件上传组件
 * @returns 组件
 */
const PictureUploader: React.FC<Props> = (props: Props) => {
  const {biz, value, onChange} = props
  const [loading, setLoading] = useState<boolean>(false)
  const uploadProps: UploadProps = {
    name: 'file',
    multiple: false,
    maxCount: 1, 
    listType: 'picture-card',
    showUploadList: false,
    disabled: loading,
    customRequest: async (fileObj: any) => {
      setLoading(true);
      try{
        const res:API.BaseResponseString_ = await uploadFileUsingPost({biz},{}, fileObj.file)
        //拼接完整路径
        const fullPath = COS_HOST + res.data;
        onChange?.(fullPath ?? '');
        fileObj.onSuccess(res.data)
      }catch(error:any){
        message.error("上传失败，"+error.message);
        fileObj.onError(error);
      }
      setLoading(false)
    },
  };
  const uploadButton = (
    <button style={{border: 0, background: 'none'}} type='button'>
      {loading? <LoadingOutlined/> : <PlusOutlined/>}
      <div style={{marginTop: 8}}>上传</div>
    </button>
  )
  return (
        <Upload {...uploadProps}>
          {value? <img alt='picture' src={value} style={{width: '100%'}}></img> : uploadButton}
        </Upload>
  );

};
export default PictureUploader;
