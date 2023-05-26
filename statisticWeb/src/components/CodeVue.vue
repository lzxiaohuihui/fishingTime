<template>
    <div>
        <DateScopePicker></DateScopePicker>
        <br />
        <br />
        <Vscode></Vscode>
        <Idea></Idea>
    </div>
</template>
  
<script>

import DateScopePicker from './code/DateScopePicker.vue';
import Vscode from './code/Vscode.vue';
import Idea from './code/Idea.vue';

export default {
    name: 'App',
    components: {
        DateScopePicker,
        Vscode,
        Idea
    }
}
</script>