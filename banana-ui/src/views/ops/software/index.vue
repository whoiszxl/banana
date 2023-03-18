<template>
  <div class="container">
    <Breadcrumb :items="['menu.list', 'menu.ops.software.list']" />
    <a-card class="general-card" :title="$t('menu.ops.software.list')">

      <!-- 头部区域 -->
      <div class="header">
        <!-- 搜索栏 -->
        <div v-if="showQuery" class="header-query">
          <a-form ref="queryRef" :model="queryParams" layout="inline">
            <a-form-item field="name" hide-label>
              <a-input
                  v-model="queryParams.softwareName"
                  placeholder="输入组件名或描述进行搜索"
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
                <a-button v-permission="['ops:software:add']" type="primary" @click="toAdd">
                  <template #icon><icon-plus/></template>新增
                </a-button>

                <a-button
                    v-permission="['ops:software:update']"
                    type="primary"
                    status="success"
                    :disabled="single"
                    :title="single ? '请选择一条要修改的数据' : ''"
                    @click="toUpdate(checkedIds[0])"
                >
                  <template #icon><icon-edit /></template>修改
                </a-button>
                <a-button
                    v-permission="['ops:software:delete']"
                    type="primary"
                    status="danger"
                    :disabled="multiple"
                    :title="multiple ? '请选择要删除的数据' : ''"
                    @click="handleBatchDelete"
                >
                  <template #icon><icon-delete /></template>删除
                </a-button>
                <a-button
                    v-permission="['ops:software:export']"
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
          :data="softwareList"
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
          <a-table-column title="ID" data-index="id" :width="30" />
          <a-table-column title="组件名" :width="80">
            <template #cell="{ record }">
              <a-link @click="toDetail(record.id)">{{
                  record.softwareName
                }}</a-link>
            </template>
          </a-table-column>

          <a-table-column title="文件名" data-index="softwareFilename" :width="100" />
          <a-table-column title="文件路径" data-index="softwarePath" :width="100" />
          <a-table-column title="安装路径" data-index="installPath" :width="100" />
          <a-table-column title="安装脚本路径" data-index="installScriptPath" :width="100" />

          <a-table-column title="创建时间" :width="100">
            <template #cell="{ record }">
              {{ record.createdAt }}
            </template>
          </a-table-column>

          <a-table-column title="操作" align="center" fixed="right" :width="100">

            <template #cell="{ record }">


              <a-button
                  v-permission="['ops:software:update']"
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
                    v-permission="['ops:software:delete']"
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
    <a-drawer :title="title" :visible="visible" :width="690" :mask-closable="false" unmount-on-close
        render-to-body @ok="handleOk" @cancel="handleCancel"
    >
    <a-form ref="formRef" :model="softwareSaveForm" :rules="rules" :label-col-style="{ width: '94px' }"
          size="large" layout="inline" auto-label-width >

        <a-form-item label="组件名" field="softwareName">
          <a-input
              v-model="softwareSaveForm.softwareName"
              placeholder="请输入组件名"
              style="width: 500px"
          />
        </a-form-item>

        <a-form-item label="组件文件名" field="softwareFilename">
          <a-input
              v-model="softwareSaveForm.softwareFilename"
              placeholder="请输入组件文件名"
              style="width: 500px"
          />
        </a-form-item>


        <a-form-item label="组件文件路径" field="softwarePath">
          <a-input
              v-model="softwareSaveForm.softwarePath"
              placeholder="请输入组件文件路径"
              style="width: 500px"
          />
        </a-form-item>

        <a-form-item label="组件安装路径" field="installPath">
          <a-input
              v-model="softwareSaveForm.installPath"
              placeholder="请输入组件安装路径"
              style="width: 500px"
          />
        </a-form-item>

        <a-form-item label="环境变量路径" field="envPath">
          <a-input
              v-model="softwareSaveForm.envPath"
              placeholder="请输入环境变量路径"
              style="width: 500px"
          />
        </a-form-item>

        <a-form-item label="安装脚本路径" field="installScriptPath">
          <a-input
              v-model="softwareSaveForm.installScriptPath"
              placeholder="请输入组件安装路径"
              style="width: 500px"
          />
        </a-form-item> 

        <a-form-item label="环境变量内容" field="envContent">
          <a-textarea
              v-model="softwareSaveForm.envContent"
              placeholder="请输入环境变量内容"
              style="width: 500px; height: 500px;"
          />
        </a-form-item>



      </a-form>
    </a-drawer>


    <a-drawer
        title="组件详情"
        :visible="detailVisible"
        :width="700"
        :footer="false"
        unmount-on-close
        render-to-body
        @cancel="handleDetailCancel"
    >
      <a-descriptions :column="1" bordered size="large">

        <a-descriptions-item label="组件名">
          <a-skeleton v-if="detailLoading" :animation="true">
            <a-skeleton-line :rows="1" />
          </a-skeleton>
          <span v-else>{{ software.softwareName }}</span>
        </a-descriptions-item>

        <a-descriptions-item label="组件文件名">
          <a-skeleton v-if="detailLoading" :animation="true">
            <a-skeleton-line :rows="1" />
          </a-skeleton>
          <span v-else>{{ software.softwareFilename }}</span>
        </a-descriptions-item>

        <a-descriptions-item label="组件文件路径">
          <a-skeleton v-if="detailLoading" :animation="true">
            <a-skeleton-line :rows="1" />
          </a-skeleton>
          <span v-else>{{ software.softwarePath }}</span>
        </a-descriptions-item>

        <a-descriptions-item label="组件安装路径">
          <a-skeleton v-if="detailLoading" :animation="true">
            <a-skeleton-line :rows="1" />
          </a-skeleton>
          <span v-else>{{ software.installPath }}</span>
        </a-descriptions-item>

        <a-descriptions-item label="环境变量路径">
          <a-skeleton v-if="detailLoading" :animation="true">
            <a-skeleton-line :rows="1" />
          </a-skeleton>
          <span v-else>{{ software.envPath }}</span>
        </a-descriptions-item>


        <a-descriptions-item label="安装脚本路径">
          <a-skeleton v-if="detailLoading" :animation="true">
            <a-skeleton-line :rows="1" />
          </a-skeleton>
          <span v-else>{{ software.installScriptPath }}</span>
        </a-descriptions-item>

        <a-descriptions-item label="创建者">
          <a-skeleton v-if="detailLoading" :animation="true">
            <a-skeleton-line :rows="1" />
          </a-skeleton>
          <span v-else>{{ software.createdBy }}</span>
        </a-descriptions-item>


        <a-descriptions-item label="更新者">
          <a-skeleton v-if="detailLoading" :animation="true">
            <a-skeleton-line :rows="1" />
          </a-skeleton>
          <span v-else>{{ software.updatedBy }}</span>
        </a-descriptions-item>

        <a-descriptions-item label="创建时间">
          <a-skeleton v-if="detailLoading" :animation="true">
            <a-skeleton-line :rows="1" />
          </a-skeleton>
          <span v-else>{{ software.createdAt }}</span>
        </a-descriptions-item>

        <a-descriptions-item label="更新时间">
          <a-skeleton v-if="detailLoading" :animation="true">
            <a-skeleton-line :rows="1" />
          </a-skeleton>
          <span v-else>{{ software.updatedAt }}</span>
        </a-descriptions-item>

                
        <a-descriptions-item label="环境变量内容">
          <a-skeleton v-if="detailLoading" :animation="true">
            <a-skeleton-line :rows="1" />
          </a-skeleton>
          <span v-else>{{ software.envContent }}</span>
        </a-descriptions-item>

      </a-descriptions>
    </a-drawer>

  </div>



</template>

<script lang="ts" setup>
  import {getCurrentInstance, reactive, ref, toRefs} from 'vue';
  import {useI18n} from 'vue-i18n';
  import useLoading from '@/hooks/loading';
  import {PolicyRecord} from '@/api/list';
  import {
    addSoftware,
    getSoftware,
    updateSoftware,
    deleteSoftware,
    SoftwareSaveCommand,
    SoftwareParam,
    SoftwareResponse,
    pageSoftware
  } from "@/api/ops/software";

  import { onBeforeUnmount, shallowRef } from 'vue'
  import { Boot } from '@wangeditor/editor'
  import markdownModule from '@wangeditor/plugin-md'


  const showQuery = ref(true);
  Boot.registerModule(markdownModule)




  type SizeProps = 'mini' | 'small' | 'medium' | 'large';
  const { proxy } = getCurrentInstance() as any;
  const { loading, setLoading } = useLoading(true);
  const { t } = useI18n();
  const renderData = ref<PolicyRecord[]>([]);

  const size = ref<SizeProps>('medium');
  const checkedIds = ref<Array<number>>([]);
  const title = ref('');
  const single = ref(true);
  const multiple = ref(true);

  const data = reactive({
    queryParams: {
      softwareName: undefined,
      page: 1,
      size: 10,
      sort: ['createdAt,desc'],
    },
    softwareSaveForm: {} as SoftwareSaveCommand,
    rules: {
      username: [{ required: true, message: '请输入管理员组件名' }],
      password: [{ required: true, message: '请输入管理员密码' }]
    },

  });
  const { queryParams, softwareSaveForm, rules } = toRefs(data);

  const softwareList = ref<SoftwareResponse[]>([]);
  const total = ref(0);

  const software = ref<SoftwareResponse>({
    id: undefined,
    softwareName: '',
    softwareFilename: '',
    softwarePath: '',
    installPath: '',
    envPath: '',
    envContent: '',
    installScriptPath: '',
    status: undefined,
    createdBy: '',
    updatedBy: '',
    createdAt: '',
    updatedAt: ''
  });

  const fetchData = (params: SoftwareParam = {...queryParams.value}) => {
    setLoading(true);
    pageSoftware(params).then(res => {
      softwareList.value = res.data.list;
      total.value = Number(res.data.total);
    }).finally(() => {
      setLoading(false);
    });

  };

  fetchData();
  const reset = () => {
    softwareSaveForm.value = {
      id: undefined,
      softwareName: '',
      softwareFilename: '',
      softwarePath: '',
      installPath: '',
      envPath: '',
      envContent: '',
      installScriptPath: '',
      status: undefined,
    };
    proxy.$refs.formRef?.reserFields();
  };

  const visible = ref(false);
  const toAdd = () => {
    reset();
    title.value = '新增组件';
    visible.value = true;
  };


  const toUpdate = (id: number) => {
    reset();
    getSoftware(id).then((res) => {
      softwareSaveForm.value = res.data as unknown as SoftwareSaveCommand;
      title.value = '修改组件';
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
        if (softwareSaveForm.value.id !== undefined) {
          updateSoftware(softwareSaveForm.value).then((res:any) => {
            handleCancel();
            fetchData();
            proxy.$message.success(res.message);
          });
        } else {
          addSoftware(softwareSaveForm.value).then((res:any) => {
            handleCancel();
            fetchData();
            proxy.$message.success(res.message);
          });
        }
      }
    });
  };

  const handleDelete = (ids: Array<number>) => {
    deleteSoftware(ids).then((res:any) => {
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
    getSoftware(id)
        .then((res) => {
          software.value = res.data;
        })
        .finally(() => {
          detailLoading.value = false;
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
        .download('/admin/ops/software/export', { ...queryParams.value }, '组件数据')
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
  name: 'SearchTable'
};
</script>

<style scoped lang="less">
.container {
  padding: 0 20px 20px 20px;
}

.header {
  padding-bottom: 10px;
}
</style>
