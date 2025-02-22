import FileUploader from '@/components/FileUploader';
import PictureUploader from '@/components/PictureUploader';
import { COS_HOST } from '@/constants';
import { testDownloadFileUsingGet, testUploadFileUsingPost } from '@/services/backend/fileController';
import { InboxOutlined } from '@ant-design/icons';
import { Button, Card, Flex, message, Upload, UploadProps, Divider } from 'antd';
import { saveAs } from 'file-saver';
import React, { useState } from 'react';

const { Dragger } = Upload;

const TestFilePage: React.FC = () => {
  const [value, setValue] = useState<string>();
  const props: UploadProps = {
    name: 'file',
    multiple: false,
    maxCount: 1,  
    customRequest: async (fileObj: any) => {
      try{
        const res:API.BaseResponseString_ = await testUploadFileUsingPost({}, fileObj.file)
        fileObj.onSuccess(res.data)
        setValue(res.data)
      }catch(error:any){
        message.error("上传失败，"+error.message);
        fileObj.onError(error);
      }
    },
    onRemove(){
      setValue(undefined)
    }
  };
  return (
    <Flex gap={16}>
      <Card title="文件上传">
        <Dragger {...props}>
          <p className="ant-upload-drag-icon">
            <InboxOutlined />
          </p>
          <p className="ant-upload-text">Click or drag file to this area to upload</p>
          <p className="ant-upload-hint">
            Support for a single or bulk upload. Strictly prohibited from uploading company data or other
            banned files.
          </p>
        </Dragger>
      </Card>
{/*       <Card title="文件下载">
        <div>文件地址：{COS_HOST + value}</div>
        <Divider/>
        <img src={COS_HOST + value} height={200}></img>
        <Divider/>
        <Button onClick={async () => {
          if (value){
            const blob = await testDownloadFileUsingGet({filepath:value},{responseType: 'blob'})
            //使用file-saver下载文件
            const fullPath:string = COS_HOST + value;
            saveAs(blob, fullPath.substring(fullPath.lastIndexOf("/") + 1))
          }else{
            message.error("文件不存在！")
          }
        }}>点击下载文件</Button>
      </Card> */}
      <Card title="上传">
        {/* <FileUploader biz='user_avatar'/> */}
        <PictureUploader biz='user_avatar'/>
      </Card>
    </Flex>
  );

};
export default TestFilePage;
