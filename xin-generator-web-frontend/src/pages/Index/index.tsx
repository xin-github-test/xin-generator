import { listGeneratorVoByPageUsingPost } from '@/services/backend/generatorController';
import { UserOutlined } from '@ant-design/icons';
import { PageContainer, ProFormSelect, ProFormText, QueryFilter } from '@ant-design/pro-components';
import { Link } from '@umijs/max';
import { message, Input, Tabs, List, Card, Typography, Avatar, Image, Tag, Flex } from 'antd';
import moment from 'moment';
import React from 'react';
import {useState,useEffect} from 'react'

/**
 * 默认分页参数
 */
const DEFAULT_PAGE_PARAMS: PageRequest = {
  current: 1,
  pageSize: 4,
  sortField: 'createTime',
  sortOrder: 'descend'
};
const IndexPage: React.FC = () => {

  const [loading, setLoading] = useState<boolean>(true);
  const [dataList, setDataList] = useState<API.GeneratorVO[]>([]);
  const [total, setTotal] = useState<number>(0);

  const { Search } = Input;
  //搜索条件
  const [searchParams, setSearchParams] = useState<API.GeneratorQueryRequest>(
    {
      ...DEFAULT_PAGE_PARAMS
    }
  );
  //搜索数据
  const doSearch = async ():Promise<void> => {
    setLoading(true);
    try {
      const res = await listGeneratorVoByPageUsingPost(searchParams);
      setDataList(res.data?.records ?? []);
      setTotal(Number(res.data?.total) ?? 0);
    } catch (error: any) {
      message.error("获取数据失败！" + error.message);
    } 
    setLoading(false);
  };

  //一旦searchParams中的值发生了改变，则会执行doSearch函数
  useEffect(():void => {
    doSearch();
  }, [searchParams]);

  /**
   * 标签列表
   * @param tags 
   * @returns 
   */
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
  

  return (
    <PageContainer title = {<></>}>
      <Flex  justify='center'>
      <Search
        style={{
          width: '40vw',
          minWidth: 320,
        }}
        placeholder="请搜索模板..."
        allowClear
        enterButton="搜索"
        size="large"
        onSearch={(value: string):void => {
          setSearchParams({
            ...DEFAULT_PAGE_PARAMS,
            searchText: value
          });
        }}
        onChange={(e) => {
          searchParams.searchText = e.target.value;
        }}
      />
      </Flex>
      <div style={{marginBottom: 16}}></div>
    <Tabs defaultActiveKey="newest"
     size='large'
     items={
      [
        {
          key: 'newest',
          label: '最新'
        },
        {
          key: 'recommend',
          label: '推荐'
        }
      ]
    } onChange={() => {}} />

      <QueryFilter
        span={12}
        labelWidth="auto"
        labelAlign='left'
        style={{ padding: '16px 0'}}
        defaultCollapsed={false}
        onFinish={(values: API.GeneratorQueryRequest): void => {
          setSearchParams({
            ...DEFAULT_PAGE_PARAMS,
            searchText: searchParams.searchText,
            ...values
          })
        }}
      >
        <ProFormSelect label="标签" name = "tags" mode='tags'/>
        <ProFormText label = "名称" name="name" />
        <ProFormText name="description" label="描述" />
      </QueryFilter>
      <div style={{marginBottom:24}}></div>

      <List<API.GeneratorVO>
      rowKey="id"
      loading={loading}
      grid={{
        gutter: 16,
        xs: 1,
        sm: 2,
        md: 3,
        lg: 3,
        xl: 4,
        xxl: 4,
      }}
      dataSource={dataList}
      pagination={{
        current: searchParams.current,
        pageSize: searchParams.pageSize,
        total,
        onChange(current: number, pageSize: number): void {
          setSearchParams({
            ...searchParams,
            current,
            pageSize
          })
        }
      }}
      renderItem={(data) => (
        <List.Item>
          <Link to={`/generator/detail/${data.id}`}>
            <Card hoverable cover={<Image style={{height:247,width:'100'}} alt={data.name} src={data.picture} />}>
              <Card.Meta
                title={<a>{data.name}</a>}
                description={
                  <Typography.Paragraph
                    ellipsis={{
                      rows: 2,
                    }}
                    style={{height: 44}}
                  >
                    {data.description}
                  </Typography.Paragraph>
                }
              />
              {tagListView(data.tags)}
              <Flex justify='space-between' align='center'>
                <Typography.Paragraph type='secondary' style={{ fontSize: 12 }}>
                  {moment(data.createTime).fromNow()}
                </Typography.Paragraph>
                  <Avatar src={data.user?.userAvatar ?? <UserOutlined/>}/>
              </Flex>
            </Card>
          </Link>
        </List.Item>
      )}
    />
    </PageContainer>
  );
}
/**
 *   const cardList = list && (
    
  );
 */
export default IndexPage;
