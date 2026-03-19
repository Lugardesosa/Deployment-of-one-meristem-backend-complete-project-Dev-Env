{{- define "config-server.fullname" -}}
{{ .Release.Name }}-{{ .Values.environment }}
{{- end }}

{{- define "config-server.name" -}}
{{ .Release.Name }}-{{ .Values.environment }}
{{- end }}

{{/* Common labels */}}
{{- define "config-server.labels" -}}
app: {{ include "config-server.name" . }}
environment: {{ .Values.environment }}
app.kubernetes.io/name: {{ include "config-server.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}

{{/* Selector labels */}}
{{- define "config-server.selectorLabels" -}}
app: {{ include "config-server.name" . }}
{{- end }}
