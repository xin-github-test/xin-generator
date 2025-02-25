import { addGeneratorUsingPost, downloadGeneratorByIdUsingGet, editGeneratorUsingPost, getGeneratorVoByIdUsingGet, updateGeneratorUsingPost, useGeneratorUsingPost } from '@/services/backend/generatorController';
import { DownloadOutlined, EditOutlined } from '@ant-design/icons';
import {
  PageContainer,
} from '@ant-design/pro-components';
import { history, Link, useModel, useParams, useSearchParams } from '@umijs/max';
import {Tabs, Button, Image, Card, Col, message, Row, Space, Typography, UploadFile, Tag, Form, Collapse } from 'antd';
import { useEffect, useRef, useState } from 'react';
import { COS_HOST } from '@/constants';
import { saveAs } from 'file-saver';


/**
 * 生成器使用页面
 * @returns 页面
 */
const GeneratorUsePage: React.FC = () => {
  //获取路径上的id (url后？连接的参数)
  // const [searchParams] = useSearchParams();
  // const id:number = Number(searchParams.get("id"));
  //获取路径上的id (动态路由后的/:id)
  const {id} = useParams();
  const [loading, setLoading] = useState<boolean>(false);
  const [downloading, setDownloading] = useState<boolean>(false);
  const [data, setData] = useState<API.GeneratorVO>({});
  const [form] = Form.useForm();

  const {initialState} = useModel('@@initialState');
  const {currentUser} = initialState?? {};
  const models = data?.modelConfig?.models?? []
  
  const loadData = async () => {
    //若是id不存在，直接返回
    if (!id) {
      return;
    }
    setLoading(true);
    //若是id存在，调用后端接口进行数据查询
    try{
      const res = await getGeneratorVoByIdUsingGet({id:Number(id)})
      setData(res.data ?? {})
    } catch (error: any) {
      message.error("数据获取失败，"+ error.message)
    }
    setLoading(false);
  };

  useEffect(() => {
    if (!id){
      return;
    }
    loadData();
  }, [id])

  const tagListView = (tags?: string[]) => {
    if (!tags) {
      return <></>;
    }
    return (
      <div style={{marginBottom: 16}}>
        {tags.map((tag: string) => (
          <Tag key={tag}>{tag}</Tag>
        ))}
      </div>
    )
  };

  /**
   * 下载按钮
   */
  const downloadButton = data.distPath && currentUser && (
    <Button
      icon={<DownloadOutlined/>}
      type='primary'
      loading = {downloading}
      onClick={async () => {
        setDownloading(true)
        const values = form.getFieldsValue();
        // debugger
        const blob = await useGeneratorUsingPost({id:data.id, dataModel: values}, {responseType: 'blob'})
        //使用file-saver下载文件
        const fullPath = COS_HOST + data.distPath
        saveAs(blob, fullPath.substring(fullPath.lastIndexOf('/') + 1))
        setDownloading(false)
      }}
    >
      生成代码
    </Button>
  )

  return (
    <PageContainer title={<></>} loading = {loading}>
      <Card>
        <Row justify='space-between' gutter={[32,32]}>
          <Col flex='auto'>
            <Space size='large' align='center'>
              <Typography.Title level={4}>{data.name}</Typography.Title>
              {tagListView(data.tags)}
            </Space>
            <Typography.Paragraph>{data.description}</Typography.Paragraph>
            <div style={{marginBottom: 24}}></div>
            <Form form={form}>
              {
                models.map((model, index) => {
                  //是分组
                  if (model.groupKey) {
                    if (!model.models){
                      return <></>
                    }
                    return <Collapse items={[
                      {
                        key: index,
                        label: model.groupName+"(分组)",
                        children: model.models?.map((subModel, index) => {
                          //@ts-ignore
                          return <Form.Item key={index} label={subModel.fieldName} name={[model.groupKey,subModel.fieldName]}>
                          <input style={{width:'50%'}} placeholder={subModel.description}/>
                        </Form.Item>
                        })
                      }
                    ]} bordered={false} defaultActiveKey={index} />
                  }
                  //@ts-ignore
                  return <Form.Item label={model.fieldName} name={[model.fieldName]}>
                      <input style={{width:'50%'}} placeholder={model.description}/>
                    </Form.Item>
                })
              }
            </Form>
            <Space size='middle'>
              {downloadButton}
              <Link to={`/generator/detail/${id}`}>
                <Button >查看详情</Button>
              </Link>
            </Space>
          </Col>

          <Col flex="320px">
            <Image src={data.picture}></Image>
          </Col>
        </Row>
      </Card>
      <div style={{marginBottom: 24}}></div>

    </PageContainer>
  );
};
export default GeneratorUsePage;