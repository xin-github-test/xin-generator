import { addGeneratorUsingPost, downloadGeneratorByIdUsingGet, editGeneratorUsingPost, getGeneratorVoByIdUsingGet, updateGeneratorUsingPost } from '@/services/backend/generatorController';
import { DownloadOutlined, EditOutlined } from '@ant-design/icons';
import {
  PageContainer,
} from '@ant-design/pro-components';
import { history, Link, useModel, useParams, useSearchParams } from '@umijs/max';
import {Tabs, Button, Image, Card, Col, message, Row, Space, Typography, UploadFile, Tag } from 'antd';
import { values } from 'lodash';
import moment from 'moment';
import { useEffect, useRef, useState } from 'react';
import FileConfig from './components/FileConfig';
import ModelConfig from './components/ModelConfig';
import AuthorInfo from './components/AuthorInfo';
import useModal from 'antd/es/modal/useModal';
import { COS_HOST } from '@/constants';
import { saveAs } from 'file-saver';


/**
 * 创建生成器页面
 * @returns 页面
 */
const GeneratorDetailPage: React.FC = () => {
  //获取路径上的id (url后？连接的参数)
  // const [searchParams] = useSearchParams();
  // const id:number = Number(searchParams.get("id"));
  //获取路径上的id (动态路由后的/:id)
  const {id} = useParams();
  const [loading, setLoading] = useState<boolean>(true);
  const [data, setData] = useState<API.GeneratorVO>({});

  const {initialState} = useModel('@@initialState');
  const {currentUser} = initialState?? {};
  const my = data?.userId === currentUser?.id
  
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
      onClick={async () => {
        const blob = await downloadGeneratorByIdUsingGet({id:Number(id)}, {responseType: 'blob'})
        //使用file-saver下载文件
        const fullPath = COS_HOST + data.distPath
        saveAs(blob, fullPath.substring(fullPath.lastIndexOf('/') + 1))
      }}
    >
      下载
    </Button>
  )

  /**
   * 编辑按钮
   */
  const editButton = my && (
    <Link to={`/generator/update?id=${data.id}`}>
      <Button icon={<EditOutlined/>}>编辑</Button>
    </Link>
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
            <Typography.Paragraph type='secondary'>创建时间：{moment(data.createTime).format('YYYY-MM-DD hh')}</Typography.Paragraph>
            <Typography.Paragraph type='secondary'>基础包：{data.basePackage}</Typography.Paragraph>
            <Typography.Paragraph type='secondary'>版本：{data.version}</Typography.Paragraph>
            <Typography.Paragraph type='secondary'>作者：{data.author}</Typography.Paragraph>
            <div style={{marginBottom: 24}}></div>
            <Space size='middle'>
              <Link to={`/generator/use/${data.id}`}>
                <Button type='primary'>立即使用</Button>
              </Link>
              {downloadButton}
              {editButton}
            </Space>
          </Col>

          <Col flex="320px">
            <Image src={data.picture}></Image>
          </Col>
        </Row>
      </Card>
      <div style={{marginBottom: 24}}></div>
      <Card>
        <Tabs
          size='large'
          defaultActiveKey={'fileConfig'}
          onChange={() => {}}
          items={[
            {
              key:'fileConfig',
              label:'文件配置',
              children:<FileConfig data={data}/>
            },
            {
              key:'modelConfig',
              label:'模型配置',
              children:<ModelConfig data={data}/>
            },
            {
              key:'userInfo',
              label:'作者信息',
              children:<AuthorInfo data={data}/>
            }
          ]}
        >

        </Tabs>
      </Card>
    </PageContainer>
  );
};
export default GeneratorDetailPage;