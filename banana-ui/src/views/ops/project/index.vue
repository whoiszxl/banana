<template>
  <div class="container">
    <Breadcrumb :items="['menu.list', 'menu.ops.project.list']" />
    <a-card class="general-card" :title="$t('menu.ops.project.list')">

      <!-- 头部区域 -->
      <div class="header">
        <!-- 搜索栏 -->
        <div v-if="showQuery" class="header-query">
          <a-form ref="queryRef" :model="queryParams" layout="inline">
            <a-form-item field="name" hide-label>
              <a-input
                  v-model="queryParams.name"
                  placeholder="输入项目名或描述进行搜索"
                  allow-clear
                  style="width: 200px"
                  @press-enter="handleQuery"
              />
            </a-form-item>

            <a-form-item hide-label>
              <a-space>
                <a-button type="primary" @click="handleQuery">
                  <template #icon><icon-search /></template>查询
                </a-button>
                <a-button @click="resetQuery">
                  <template #icon><icon-refresh /></template>重置
                </a-button>
              </a-space>
            </a-form-item>
          </a-form>
        </div>
        <!-- 操作栏 -->
        <div class="header-operation">
          <a-row>
            <a-col :span="16">
              <a-space>
                <a-button v-permission="['ops:project:add']" type="primary" @click="toAdd">
                  <template #icon><icon-plus/></template>新增
                </a-button>

                <a-button
                    v-permission="['ops:project:update']"
                    type="primary"
                    status="success"
                    :disabled="single"
                    :title="single ? '请选择一条要修改的数据' : ''"
                    @click="toUpdate(checkedIds[0])"
                >
                  <template #icon><icon-edit /></template>修改
                </a-button>
                <a-button
                    v-permission="['ops:project:delete']"
                    type="primary"
                    status="danger"
                    :disabled="multiple"
                    :title="multiple ? '请选择要删除的数据' : ''"
                    @click="handleBatchDelete"
                >
                  <template #icon><icon-delete /></template>删除
                </a-button>
                <a-button
                    v-permission="['ops:project:export']"
                    :loading="exportLoading"
                    type="primary"
                    status="warning"
                    @click="handleExport"
                >
                  <template #icon><icon-download /></template>导出
                </a-button>
              </a-space>
            </a-col>
            <a-col :span="8">
              <right-toolbar v-model:show-query="showQuery" @refresh="fetchData"/>
            </a-col>
          </a-row>
        </div>
      </div>


      <a-table
          row-key="id"
          :data="projectList"
          :loading="loading"
          :pagination="{
              showTotal: true,
              showPageSize: true,
              total: total,
              current: queryParams.page,
            }"
          :bordered="false"
          :size="size"
          @page-change="handlePageChange"
          @page-size-change="handlePageSizeChange"
          @selection-change="handleSelectionChange"
          :row-selection="{
              type: 'checkbox',
              showCheckedAll: true,
              onlyCurrent: false,
            }"
      >

        <template #columns>
          <a-table-column title="ID" data-index="id" :width="85" />
          <a-table-column title="项目名" :width="150">
            <template #cell="{ record }">
              <a-link @click="toDetail(record.id)">{{
                  record.name
                }}</a-link>
            </template>
          </a-table-column>

          <a-table-column title="描述" data-index="description" :width="240" />


          <a-table-column title="平台" :width="100">
            <template #cell="{ record }">
              <span v-if="record.platformType === 1">自建</span>
              <span v-else-if="record.platformType === 2">阿里云</span>
              <span v-else-if="record.platformType === 3">腾讯云</span>
              <span v-else>未知</span>
            </template>
          </a-table-column>

          <a-table-column title="创建时间" :width="130">
            <template #cell="{ record }">
              {{ record.createdAt }}
            </template>
          </a-table-column>

          <a-table-column title="操作" align="center" fixed="right" :width="250">

            <template #cell="{ record }">

              
              <a-popconfirm content="确定生成项目下的相关实例吗？" type="warning" @ok="handleGenerate(record.id)">
                
                <a-button
                    v-permission="['ops:project:generate']"
                    type="text"
                    size="small"
                    title="实例生成"
                    :disabled="record.disabled"
                >

                  <template #icon><icon-cloud /></template>实例生成
                </a-button>
              </a-popconfirm>


              <a-button
                  v-permission="['ops:project:update']"
                  type="text"
                  size="small"
                  title="修改"
                  @click="toUpdate(record.id)"
              >
                <template #icon><icon-edit /></template>修改
              </a-button>

              <a-popconfirm
                  content="确定删除吗？"
                  type="warning"
                  @ok="handleDelete([record.id])"
              >
                <a-button
                    v-permission="['ops:project:delete']"
                    type="text"
                    size="small"
                    title="删除"
                    :disabled="record.disabled"
                >
                  <template #icon><icon-delete /></template>删除
                </a-button>
              </a-popconfirm>
            </template>

          </a-table-column>
        </template>


      </a-table>
    </a-card>




    <!-- 表单区域 -->
    <a-drawer
        :title="title"
        :visible="visible"
        :width="1080"
        :mask-closable="false"
        unmount-on-close
        render-to-body
        @ok="handleOk"
        @cancel="handleCancel"
    >
    <a-form
          ref="formRef"
          :model="projectSaveForm"
          :rules="rules"
          :label-col-style="{ width: '94px' }"
          size="large"
          layout="inline"
          auto-label-width
      >
        <a-form-item label="项目名" field="name">
          <a-input
              v-model="projectSaveForm.name"
              placeholder="请输入项目名"
              style="width: 162px"
          />
        </a-form-item>

        <a-form-item label="平台" field="platformType">
          <a-radio-group v-model="projectSaveForm.platformType">
            <a-radio :value="1">自建</a-radio>
            <a-radio :value="2">阿里云</a-radio>
            <a-radio :value="3">腾讯云</a-radio>
          </a-radio-group>
        </a-form-item>
        
        <a-form-item label="描述" field="description">
          <a-textarea
              v-model="projectSaveForm.description"
              placeholder="请输入描述"
              style="width: 912px"
          />
        </a-form-item>

        <a-form-item :width="1080" label="文档" field="markdown">
          <div style="border: 1px solid #ccc">
            <Editor style="height: 500px; width: 912px; overflow-y: hidden;" v-model="projectSaveForm.markdown" :defaultConfig="editorConfig"/>
          </div>
        </a-form-item>


        <a-form-item :width="1080" label="平台参数" field="platformParams">
          <a-textarea
              allow-clear
              v-model="projectSaveForm.platformParams"
              placeholder="请输入平台参数"
              style="width: 912px;height: 480px;"
          />
        </a-form-item>


      </a-form>
    </a-drawer>


    <a-drawer
        title="项目详情"
        :visible="detailVisible"
        :width="700"
        :footer="false"
        unmount-on-close
        render-to-body
        @cancel="handleDetailCancel"
    >
      <a-descriptions :column="2" bordered size="large">

        <a-descriptions-item label="项目名">
          <a-skeleton v-if="detailLoading" :animation="true">
            <a-skeleton-line :rows="1" />
          </a-skeleton>
          <span v-else>{{ project.name }}</span>
        </a-descriptions-item>

        <a-descriptions-item label="描述">
          <a-skeleton v-if="detailLoading" :animation="true">
            <a-skeleton-line :rows="1" />
          </a-skeleton>
          <span v-else>{{ project.description }}</span>
        </a-descriptions-item>

        <a-descriptions-item label="创建者">
          <a-skeleton v-if="detailLoading" :animation="true">
            <a-skeleton-line :rows="1" />
          </a-skeleton>
          <span v-else>{{ project.createdBy }}</span>
        </a-descriptions-item>

        <a-descriptions-item label="创建时间">
          <a-skeleton v-if="detailLoading" :animation="true">
            <a-skeleton-line :rows="1" />
          </a-skeleton>
          <span v-else>{{ project.createdAt }}</span>
        </a-descriptions-item>


        <a-descriptions-item label="更新者">
          <a-skeleton v-if="detailLoading" :animation="true">
            <a-skeleton-line :rows="1" />
          </a-skeleton>
          <span v-else>{{ project.updatedBy }}</span>
        </a-descriptions-item>

        <a-descriptions-item label="更新时间">
          <a-skeleton v-if="detailLoading" :animation="true">
            <a-skeleton-line :rows="1" />
          </a-skeleton>
          <span v-else>{{ project.updatedAt }}</span>
        </a-descriptions-item>


        <a-descriptions-item label="文档">
          <a-skeleton v-if="detailLoading" :animation="true">
            <a-skeleton-line :rows="1" />
          </a-skeleton>

          <span v-else>
            <Editor style="height: 500px; overflow-y: hidden;" v-model="project.markdown" :defaultConfig="editorConfig"/>
          </span>
        </a-descriptions-item>

      </a-descriptions>

      <div style="margin-top: 10px;">
        <a-descriptions  v-for="(item, key) in serverList" v-bind:key="key" style="" :column="1" bordered size="large">


          <a-descriptions-item label="服务器">
            <a-skeleton v-if="detailLoading" :animation="true"> <a-skeleton-line :rows="1" /> </a-skeleton>
            <span v-else>
                <a-typography-paragraph copyable>外网IP: {{ item.serverOuterIp }}</a-typography-paragraph>
                <a-typography-paragraph copyable>内网IP: {{ item.serverInnerIp }}</a-typography-paragraph>
                <a-typography-paragraph copyable>用户名： {{ item.serverUsername }}</a-typography-paragraph>
                <a-typography-paragraph copyable>密码： {{ item.serverPassword }}</a-typography-paragraph>
            </span>
          </a-descriptions-item>
        </a-descriptions>
      </div>
    </a-drawer>

  </div>



</template>

<script lang="ts" setup>
  import {getCurrentInstance, reactive, ref, toRefs} from 'vue';
  import {useI18n} from 'vue-i18n';
  import useLoading from '@/hooks/loading';
  import {
    addProject,
    getProject,
    updateProject,
    deleteProject,
    ProjectSaveCommand,
    ProjectParam,
    ProjectResponse,
    pageProject,
    generateProject
  } from "@/api/ops/project";
  import '@wangeditor/editor/dist/css/style.css' // 引入 css

  import { onBeforeUnmount, shallowRef, onMounted } from 'vue'
  import { Boot } from '@wangeditor/editor'
  import markdownModule from '@wangeditor/plugin-md'
  import { Editor } from '@wangeditor/editor-for-vue'
  import { listServer } from '@/api/ops/server';
  import { ServerResponse } from "@/api/ops/server";
  import { Notification } from '@arco-design/web-vue';


  const showQuery = ref(true);
  Boot.registerModule(markdownModule)

  type SizeProps = 'mini' | 'small' | 'medium' | 'large';
  const { proxy } = getCurrentInstance() as any;
  const { loading, setLoading } = useLoading(true);
  const { t } = useI18n();

  const size = ref<SizeProps>('medium');
  const checkedIds = ref<Array<number>>([]);
  const title = ref('');
  const single = ref(true);
  const multiple = ref(true);

  const data = reactive({
    queryParams: {
      name: undefined,
      page: 1,
      size: 10,
      sort: ['createdAt,desc'],
    },
    projectSaveForm: {} as ProjectSaveCommand,
    rules: {
      name: [{ required: true, message: '请输入项目名称' }],
      platformParams: [{ required: true, message: '请输入平台参数' }]
    },

  });
  const { queryParams, projectSaveForm, rules } = toRefs(data);

  const projectList = ref<ProjectResponse[]>([]);
  const total = ref(0);

  const project = ref<ProjectResponse>({
    id: 0,
    name: '',
    description: '',
    markdown: '',
    platformType: undefined,
    platformParams: '',
    createdBy: '',
    updatedBy: '',
    createdAt: '',
    updatedAt: '',
  });

  const serverList = ref<ServerResponse[]>([]);

  const fetchData = (params: ProjectParam = {...queryParams.value}) => {
    setLoading(true);
    pageProject(params).then(res => {
      projectList.value = res.data.list;
      total.value = Number(res.data.total);
    }).finally(() => {
      setLoading(false);
    });

  };

  fetchData();
  const reset = () => {
    projectSaveForm.value = {
      id: undefined,
      name: '',
      description: '',
      markdown: '',
      platformType: undefined,
      platformParams: '',
    };
    proxy.$refs.formRef?.reserFields();
  };

  const visible = ref(false);
  const toAdd = () => {
    reset();
    title.value = '新增项目';
    visible.value = true;
  };

  const toUpdate = (id: number) => {
    reset();
    getProject(id).then((res) => {
      projectSaveForm.value = res.data as unknown as ProjectSaveCommand;
      title.value = '修改项目';
      visible.value = true;
    });
  };


  const handleQuery = () => {
    console.log("handleQuery", queryParams);
    fetchData();
  };

  const resetQuery = () => {
    proxy.$refs.queryRef.resetFields();
    handleQuery();
  };

  const handlePageChange = (current: number) => {
    queryParams.value.page = current;
    fetchData();
  };

  const handlePageSizeChange = (pageSize: number) => {
    queryParams.value.size = pageSize;
    fetchData();
  };

  const handleSelectionChange = (rowKeys: Array<any>) => {
    checkedIds.value = rowKeys;
    single.value = rowKeys.length !== 1;
    multiple.value = !rowKeys.length;
  };


  const handleCancel = () => {
    visible.value = false;
    proxy.$refs.formRef?.resetFields();
    proxy.$refs.userRoleFormRef?.resetFields();
  };

  const handleOk = () => {
    proxy.$refs.formRef.validate((valid: any) => {
      if (!valid) {
        if (projectSaveForm.value.id !== undefined) {
          updateProject(projectSaveForm.value).then((res:any) => {
            handleCancel();
            fetchData();
            proxy.$message.success(res.message);
          });
        } else {
          addProject(projectSaveForm.value).then((res:any) => {
            handleCancel();
            fetchData();
            proxy.$message.success(res.message);
          });
        }
      }
    });
  };

  const handleDelete = (ids: Array<number>) => {
    deleteProject(ids).then((res:any) => {
      proxy.$message.success(res.message);
      fetchData();
    });
  };

  const handleGenerate = (id: number) => {
    Notification.info({
                title: '提示',
                content:"服务器正在努力生成中，请稍等！！",
                duration: 15000,
                closable: true,
            });
    generateProject(id).then((res:any) => {
      proxy.$message.success(res.message);
      fetchData();
    });
  };
  

  const detailLoading = ref(false);
  const detailVisible = ref(false);
  const toDetail = async (id: number) => {
    if (detailLoading.value) return;
    detailLoading.value = true;
    detailVisible.value = true;
    getProject(id)
        .then((res) => {
          project.value = res.data;
        })
        .finally(() => {
          detailLoading.value = false;
        });

        listServer({ projectId: id }).then((res) => {
          serverList.value = res.data;
        });

    
  };

  const handleDetailCancel = () => {
    detailVisible.value = false;
  };

  const handleBatchDelete = () => {
    if(checkedIds.value.length === 0) {
      proxy.$message.info('没有选中数据');
    }else {
      proxy.$modal.warning({
        title: '警告',
        titleAlign: "start",
        content: '确定删除吗？',
        hideCancel: false,
        onOk: () => {
          handleDelete(checkedIds.value);
        }
      });
    }
  }

  const exportLoading = ref(false);
  const handleExport = () => {
    if(exportLoading.value) return;

    exportLoading.value = true;
    proxy
        .download('/admin/ops/project/export', { ...queryParams.value }, '项目数据')
        .finally(() => {
          exportLoading.value = false;
        });

  }




  // 编辑器实例，必须用 shallowRef
  const editorRef = shallowRef()

  const editorConfig = { placeholder: '请输入内容...' }

  // 组件销毁时，也及时销毁编辑器
  onBeforeUnmount(() => {
      const editor = editorRef.value
      if (editor == null) return
      editor.destroy()
  })

</script>

<script lang="ts">
export default {
  name: 'SearchTable',
  components: { Editor }
};
</script>

<style scoped lang="less">
.container {
  padding: 0 20px 20px 20px;
}

.header {
  padding-bottom: 10px;
}

a-typography-paragraph * {
  line-height: inherit;
}
</style>
