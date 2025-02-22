import { COS_HOST } from '@/constants';
import { testDownloadFileUsingGet, testUploadFileUsingPost, uploadFileUsingPost } from '@/services/backend/fileController';
import { InboxOutlined } from '@ant-design/icons';
import { Button, Card, Flex, message, Upload, UploadProps, Divider } from 'antd';
import { UploadFile } from 'antd/lib';
import { saveAs } from 'file-saver';
import React, { useState } from 'react';

const { Dragger } = Upload;

interface Props {
  biz: string;
  onChange?: (fileList: UploadFile[]) => void;
  value?: UploadFile[];
  description?: string;
}
/**
 * 文件上传组件
 * @returns 组件
 */
const FileUploader: React.FC<Props> = (props: Props) => {
  const {biz, value, description, onChange} = props
  const [loading, setLoading] = useState<boolean>(false)
  const uploadProps: UploadProps = {
    name: 'file',
    multiple: false,
    maxCount: 1, 
    fileList: value,
    disabled: loading,
    onChange({fileList}){
      onChange?.(fileList)
    },
    customRequest: async (fileObj: any) => {
      setLoading(true);
      try{
        const res:API.BaseResponseString_ = await uploadFileUsingPost({biz},{}, fileObj.file)
        fileObj.onSuccess(res.data)
      }catch(error:any){
        message.error("上传失败，"+error.message);
        fileObj.onError(error);
      }
      setLoading(false)
    },
  };
  return (
        <Dragger {...uploadProps}>
          <p className="ant-upload-drag-icon">
            <InboxOutlined />
          </p>
          <p className="ant-upload-text">点击或拖拽上传！</p>
          <p className="ant-upload-hint">
            {description}
          </p>
        </Dragger>
  );

};
export default FileUploader;
